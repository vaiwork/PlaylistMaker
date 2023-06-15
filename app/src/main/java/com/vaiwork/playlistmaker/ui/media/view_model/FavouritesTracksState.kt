package com.vaiwork.playlistmaker.ui.media.view_model

sealed interface FavouritesTracksState {
    object ErrorYourMediaEmpty : FavouritesTracksState
}