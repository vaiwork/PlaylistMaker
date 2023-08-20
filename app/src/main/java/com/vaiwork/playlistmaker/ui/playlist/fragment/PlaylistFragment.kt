package com.vaiwork.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.databinding.FragmentPlaylistBinding
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.vaiwork.playlistmaker.ui.media.fragment.PlaylistsFragment
import com.vaiwork.playlistmaker.ui.playlist.view_model.PlaylistState
import com.vaiwork.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.vaiwork.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var playlistTrackAdapter: PlaylistTrackAdapter = PlaylistTrackAdapter()

    private lateinit var albumImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var calcMinutesTextView: TextView
    private lateinit var calcTracksTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetLinearLayout: LinearLayout
    private lateinit var buttonsLinearLayout: LinearLayout

    private lateinit var onPlaylistTrackClickDebounce: (Track) -> Unit

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        private const val ARGS_PLAYLIST: String = "PLAYLIST_STRING"
        fun createArgs(playlistString: String): Bundle = bundleOf(ARGS_PLAYLIST to playlistString)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumImageView = binding.fragmentPlaylistAlbum
        nameTextView = binding.fragmentPlaylistName
        descriptionTextView = binding.fragmentPlaylistDescription
        calcMinutesTextView = binding.fragmentPlaylistCalcMinutes
        calcTracksTextView = binding.fragmentPlaylistCalcTracks
        recyclerView = binding.fragmentPlaylistRecyclerView
        buttonsLinearLayout = binding.fragmentPlaylistButtons
        bottomSheetLinearLayout = binding.fragmentPlaylistBottomSheet

        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) { state ->
            when(state) {
                is PlaylistState.Changed -> setUI(state.playlist)
                is PlaylistState.Default -> { }
            }
        }

        val playlist: Playlist = getPlaylist()
        setUI(playlist)

        onPlaylistTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            findNavController().navigate(R.id.action_playlistFragment_to_audioPlayerActivity, AudioPlayerActivity.createArgs(track.trackId))
        }

        binding.fragmentPlaylistPanelHeade.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getPlaylist(): Playlist {
        return requireArguments().getString(ARGS_PLAYLIST)
            ?.let { playlistViewModel.mapStringToPlaylist(it) }!!
    }

    private fun setUI(playlist: Playlist) {
        Glide
            .with(albumImageView)
            .load(playlist.playlistCoverLocalUri)
            .placeholder(R.drawable.placeholder_album_image_light_mode)
            .into(albumImageView)
        if (playlist.playlistCoverLocalUri != "") {
            albumImageView.scaleType = ScaleType.FIT_XY
        }
        nameTextView.text = playlist.playlistTitle
        descriptionTextView.text = playlist.playlistDescription
        calcTracksTextView.text = playlistViewModel.calcNumberTracks(playlist.playlistTracksNumber)
        playlistViewModel.observeTracksMinutesCalcLiveData().observe(viewLifecycleOwner) { it ->
            calcMinutesTextView.text = it
        }
        playlistViewModel.calcMinutesTracks(playlist.playlistTracks)

        playlistViewModel.observeTracksLiveData().observe(viewLifecycleOwner) { it ->
            playlistTrackAdapter.setTracks(it)
            playlistTrackAdapter.notifyDataSetChanged()
            recyclerView.adapter = playlistTrackAdapter
            playlistTrackAdapter.setItemClickListener {
                onPlaylistTrackClickDebounce(it)
            }
            playlistTrackAdapter.setItemLongClickListener { track ->
                MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle("Хотите удалить трек?")
                    .setNegativeButton("Нет") { _, _ ->
                    }
                    .setPositiveButton("Да") { _, _ ->
                        // delete trackId from this playlist, check that this is don't use in other playlists and delete from playlist_track
                        playlistViewModel.deleteTrackFromPlaylist(playlist, track)
                    }
                    .show()
                true
            }
        }
        playlistViewModel.getPlaylistTracks(playlist.playlistTracks)
    }
}