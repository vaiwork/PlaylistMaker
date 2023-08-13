package com.vaiwork.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.databinding.FragmentPlaylistsBinding
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsState
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private var binding: FragmentPlaylistsBinding? = null

    private var playlistsAdapter: PlaylistsAdapter = PlaylistsAdapter()

    private lateinit var newPlaylistButton: Button
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance() = PlaylistsFragment()
            .apply { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newPlaylistButton = binding!!.fragmentPlaylistsNewPlaylistButton

        newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistActivity2)
        }

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.ErrorYouDoNotCreateAnyPlaylists -> { showPlaylists(false); showError() }
                is PlaylistsState.ContentPlaylists ->  viewPlaylists(it.playlists)
            }
        }

        recyclerView = binding!!.fragmentPlaylistsRecyclerView
        recyclerView.adapter = playlistsAdapter
        recyclerView.visibility = View.GONE
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        playlistsViewModel.isPlaylistsEmptyCheck()
    }

    private fun viewPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.setPlaylists(playlists)
        recyclerView.adapter = playlistsAdapter
        showPlaylists(true)
        hideError()
    }

    private fun showPlaylists(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showError() {
        binding?.apply {
            fragmentPlaylistsPlaceholderImageView.isVisible = true
            fragmentPlaylistsPlaceholderTextView.isVisible = true
        }
    }

    private fun hideError() {
        binding?.apply {
            fragmentPlaylistsPlaceholderImageView.isVisible = false
            fragmentPlaylistsPlaceholderTextView.isVisible = false
        }
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.isPlaylistsEmptyCheck()
    }
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        playlistsViewModel.isPlaylistsEmptyCheck()
    }
}