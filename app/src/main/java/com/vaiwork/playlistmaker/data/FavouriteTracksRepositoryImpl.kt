package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter,
): FavouriteTracksRepository {
    override fun getFavouritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTracksDao().selectAllTracks()
        if (tracks != null) {
            convertFromTrackEntity(tracks.sortedBy { track: TrackEntity -> track.addedDateTime }.reversed())?.let {
                emit(
                    it
                )
            }
        } else {
            emit(emptyList())
        }
    }

    override suspend fun addTrackToFavourite(track: Track) {
        val trackEntity = dbConverter.map(track)
        if (trackEntity != null)
            appDatabase.favouriteTracksDao().insertTracks(listOf(trackEntity))
        else
            appDatabase.favouriteTracksDao().insertTracks(emptyList())
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        val trackEntity = dbConverter.map(track)
        if (trackEntity != null)
            appDatabase.favouriteTracksDao().deleteTrackEntity(trackEntity)
    }

    override fun selectTrackByTrackId(trackId: Int): Flow<Track?> = flow {
        emit(dbConverter.map(appDatabase.favouriteTracksDao().selectTrackByTrackId(trackId)))
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>?): List<Track>? {
        return tracks?.map { track -> dbConverter.map(track)!! }
    }

}