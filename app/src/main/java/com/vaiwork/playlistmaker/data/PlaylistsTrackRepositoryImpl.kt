package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.PlaylistsTrackEntity
import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackRepository
import com.vaiwork.playlistmaker.domain.models.Track

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
}