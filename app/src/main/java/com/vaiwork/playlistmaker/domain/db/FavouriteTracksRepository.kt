package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    fun getFavouritesTracks(): Flow<List<Track>?>
    suspend fun addTrackToFavourite(track: Track)
    suspend fun deleteTrackFromFavourite(track: Track)
    fun selectTrackByTrackId(trackId: Int): Flow<Track?>
}