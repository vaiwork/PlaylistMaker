package com.vaiwork.playlistmaker.ui.playlist.view_model

import com.vaiwork.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    data class Changed(val playlist: Playlist): PlaylistState

    object Default: PlaylistState
}