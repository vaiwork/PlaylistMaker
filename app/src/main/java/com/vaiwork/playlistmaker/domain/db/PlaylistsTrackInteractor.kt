package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsTrackInteractor {
    suspend fun insertTrackToPlaylist(track: Track)

    fun getTracksByIds(tracksIds: List<Int>): Flow<List<Track>>

    suspend fun deleteTrackRow(trackId: Int)
}