package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity
import com.vaiwork.playlistmaker.domain.db.PlaylistsRepository
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter,
): PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>?> = flow {
        val playlists = appDatabase.playlistsDao().selectAllPlaylists()
        if (playlists != null) {
            convertFromPlaylistEntity(
                playlists.sortedByDescending  { playlist: PlaylistEntity -> playlist.addedDateTime }
            )?.let { emit(it) }
        } else {
            emit(emptyList())
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistEntity: PlaylistEntity? = dbConverter.map(playlist)
        if (playlistEntity == null) {
            appDatabase.playlistsDao().insertPlaylists(emptyList())
        } else {
            appDatabase.playlistsDao().insertPlaylists(listOf(playlistEntity))
        }
    }

    override fun updatePlaylistRow(playlist: Playlist, trackId: Int): Flow<Int> = flow {
        val playlists: List<PlaylistEntity>? = appDatabase.playlistsDao().selectAllPlaylists()
        for (_playlist in playlists!!) {
            if (dbConverter.map(_playlist)?.equals(playlist) == true) {
                var tracks: List<Int> = playlist.playlistTracks
                if (!tracks.contains(trackId)) {
                    tracks += trackId
                    val newPlaylist = Playlist(
                        playlist.playlistTitle,
                        playlist.playlistDescription,
                        playlist.playlistCoverLocalUri,
                        tracks,
                        tracks.size
                    )
                    dbConverter.map(newPlaylist, _playlist.playlistId, _playlist.addedDateTime)
                        ?.let { appDatabase.playlistsDao().updatePlaylistRow(it) }
                    emit(0)
                } else {
                    emit(1)
                }
                break
            }
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>?): List<Playlist>? {
        return playlists?.map { playlist -> dbConverter.map(playlist)!! }
    }
}