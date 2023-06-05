package com.vaiwork.playlistmaker.ui.media.view_model

sealed interface PlaylistsState {
    data class Error(
        val message: String
    ) : PlaylistsState
}