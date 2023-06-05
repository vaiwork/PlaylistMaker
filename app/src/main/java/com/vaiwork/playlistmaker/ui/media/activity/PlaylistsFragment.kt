package com.vaiwork.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vaiwork.playlistmaker.databinding.FragmentPlaylistsBinding
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsState
import com.vaiwork.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistsState.Error -> showError(it.message)
            }
        }
    }

    private fun showError(message: String) {
        binding.apply {
            fragmentPlaylistsNewPlaylistButton.visibility = View.VISIBLE
            fragmentPlaylistsPlaceholderImageView.visibility = View.VISIBLE
            fragmentPlaylistsPlaceholderTextView.visibility = View.VISIBLE
        }
    }
}