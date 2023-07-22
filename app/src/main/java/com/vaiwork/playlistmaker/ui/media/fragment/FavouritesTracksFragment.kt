package com.vaiwork.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.databinding.FragmentFavouritesTracksBinding
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.vaiwork.playlistmaker.ui.media.view_model.FavouritesTracksState
import com.vaiwork.playlistmaker.ui.media.view_model.FavouritesTracksViewModel
import com.vaiwork.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesTracksFragment : Fragment() {

    private val favouritesTracksViewModel: FavouritesTracksViewModel by viewModel()
    private lateinit var binding: FragmentFavouritesTracksBinding

    private var favouriteTracksAdapter: FavouriteTracksAdapter = FavouriteTracksAdapter()

    private lateinit var recyclerView: RecyclerView

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    companion object {
        fun newInstance() = FavouritesTracksFragment()
            .apply { }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            //favouritesTracksViewModel.onDebounce(track)
            findNavController().navigate(R.id.action_mediaFragment_to_audioPlayerActivity, AudioPlayerActivity.createArgs(track.trackId))
        }

        recyclerView = binding.fragmentYourMediaRecyclerView

        favouriteTracksAdapter.setItemClickListener {
            onTrackClickDebounce(it)
        }

        favouritesTracksViewModel.favouriteTracksMediaControl()

        favouritesTracksViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavouritesTracksState.ErrorYourMediaEmpty -> showError()
                is FavouritesTracksState.MediaNotEmpty -> showFavouriteTracks(it.tracks)
            }
        }
    }

    private fun showFavouriteTracks(tracks: List<Track>) {
        hideError()
        showTracksList(true)
        favouriteTracksAdapter.setTracks(tracks)
        favouriteTracksAdapter.notifyDataSetChanged()
        recyclerView.adapter = favouriteTracksAdapter
    }

    private fun showTracksList(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError() {
        showTracksList(false)
        binding.apply {
            fragmentYourMediaEmptyPlaceholderImageView.isVisible = true
            fragmentYourMediaEmptyPlaceholderTextView.isVisible = true
        }
    }

    private fun hideError() {
        binding.apply {
            fragmentYourMediaEmptyPlaceholderImageView.isVisible = false
            fragmentYourMediaEmptyPlaceholderTextView.isVisible = false
        }
    }

    override fun onResume() {
        super.onResume()
        favouritesTracksViewModel.favouriteTracksMediaControl()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        favouritesTracksViewModel.favouriteTracksMediaControl()
    }
}