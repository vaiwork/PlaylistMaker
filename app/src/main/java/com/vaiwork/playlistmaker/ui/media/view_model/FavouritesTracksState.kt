package com.vaiwork.playlistmaker.ui.media.view_model

sealed interface FavouritesTracksState {
    data class Error(
        val message: String
    ) : FavouritesTracksState
}