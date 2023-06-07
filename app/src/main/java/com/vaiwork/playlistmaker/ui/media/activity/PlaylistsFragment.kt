package com.vaiwork.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vaiwork.playlistmaker.databinding.FragmentPlaylistsBinding
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsState
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private var binding: FragmentPlaylistsBinding? = null

    companion object{
        fun newInstance() = PlaylistsFragment()
            .apply { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistsState.ErrorYouDoNotCreateAnyPlaylists -> showError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showError() {
        binding?.apply {
            fragmentPlaylistsNewPlaylistButton.isVisible = true
            fragmentPlaylistsPlaceholderImageView.isVisible = true
            fragmentPlaylistsPlaceholderTextView.isVisible = true
        }
    }
}