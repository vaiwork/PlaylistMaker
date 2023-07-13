package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
): FavouriteTracksRepository {
    override fun getFavouritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTracksDao().selectAllTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addTrackToFavourite(track: Track) {
        appDatabase.favouriteTracksDao().insertTracks(listOf(trackDbConverter.map(track)))
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        appDatabase.favouriteTracksDao().deleteTrackEntity(trackDbConverter.map(track))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}