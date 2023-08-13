package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Track

interface PlaylistsTrackInteractor {
    suspend fun insertTrackToPlaylist(track: Track)
}