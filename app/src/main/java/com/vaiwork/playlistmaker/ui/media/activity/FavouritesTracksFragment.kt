package com.vaiwork.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vaiwork.playlistmaker.databinding.FragmentFavouritesTracksBinding
import com.vaiwork.playlistmaker.ui.media.view_model.FavouritesTracksState
import com.vaiwork.playlistmaker.ui.media.view_model.FavouritesTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesTracksFragment : Fragment() {

    private val favouritesTracksViewModel: FavouritesTracksViewModel by viewModel()
    private lateinit var binding: FragmentFavouritesTracksBinding

    companion object {
        fun newInstance() = FavouritesTracksFragment()
            .apply { }
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

        favouritesTracksViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavouritesTracksState.ErrorYourMediaEmpty -> showError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
    }

    private fun showError() {
        binding.apply {
            fragmentYourMediaEmptyPlaceholderImageView.isVisible = true
            fragmentYourMediaEmptyPlaceholderTextView.isVisible = true
        }
    }
}