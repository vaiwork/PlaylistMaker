package com.vaiwork.playlistmaker.ui.media.view_model

sealed interface PlaylistsState {
    object ErrorYouDoNotCreateAnyPlaylists : PlaylistsState
}