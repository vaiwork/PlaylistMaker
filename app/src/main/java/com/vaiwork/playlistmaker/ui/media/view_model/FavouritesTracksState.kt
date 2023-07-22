package com.vaiwork.playlistmaker.ui.media.view_model

import com.vaiwork.playlistmaker.domain.models.Track

sealed interface FavouritesTracksState {
    object ErrorYourMediaEmpty : FavouritesTracksState
    data class MediaNotEmpty(val tracks: List<Track>): FavouritesTracksState
}