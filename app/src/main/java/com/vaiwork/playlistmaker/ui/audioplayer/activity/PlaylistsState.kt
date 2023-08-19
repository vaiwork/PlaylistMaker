package com.vaiwork.playlistmaker.ui.audioplayer.activity

import com.vaiwork.playlistmaker.domain.models.Playlist

sealed interface PlaylistsState {
    object Default: PlaylistsState
    data class Content(var playlists: List<Playlist>) : PlaylistsState
}