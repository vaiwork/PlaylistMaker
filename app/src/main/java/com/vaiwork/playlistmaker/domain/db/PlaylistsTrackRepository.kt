package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsTrackRepository {
    suspend fun insertTrackToPlaylist(track: Track)
    fun getTracksByIds(tracksIds: List<Int>): Flow<List<Track>>
}