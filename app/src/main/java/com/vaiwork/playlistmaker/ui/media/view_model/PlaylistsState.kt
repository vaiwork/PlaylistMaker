package com.vaiwork.playlistmaker.ui.media.view_model

import com.vaiwork.playlistmaker.domain.models.Playlist

sealed interface PlaylistsState {
    object ErrorYouDoNotCreateAnyPlaylists : PlaylistsState

    data class ContentPlaylists(
        val playlists: List<Playlist>
    ) : PlaylistsState
}