package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Track

interface PlaylistsTrackRepository {
    suspend fun insertTrackToPlaylist(track: Track)
}