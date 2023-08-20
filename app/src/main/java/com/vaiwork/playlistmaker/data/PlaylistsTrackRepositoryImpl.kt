package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.PlaylistsTrackEntity
import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter,
): PlaylistsTrackRepository {
    override suspend fun insertTrackToPlaylist(track: Track) {
        val playlistsTrackEntity: PlaylistsTrackEntity? = dbConverter.pmap(track)
        if (playlistsTrackEntity == null) {
            appDatabase.playlistsTrackDao().insertTrackToPlaylist(emptyList())
        } else {
            appDatabase.playlistsTrackDao().insertTrackToPlaylist(listOf(playlistsTrackEntity))
        }
    }

    override fun getTracksByIds(tracksIds: List<Int>): Flow<List<Track>> = flow {
        var tracks: List<Track> = emptyList()
        for (trackId in tracksIds) {
            var track = appDatabase.playlistsTrackDao().selectPlaylistsTrackById(trackId)
            tracks += dbConverter.pmap(track)!!
        }
        emit(tracks)
    }
}