package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity
import com.vaiwork.playlistmaker.domain.db.PlaylistsRepository
import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
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

    override fun addPlaylist(playlist: Playlist): Flow<Int> = flow {
        var playlistExist = false
        val playlists: List<PlaylistEntity>? = appDatabase.playlistsDao().selectAllPlaylists()
        for (_playlist in playlists!!) {
            if (dbConverter.map(_playlist)?.equals(playlist) == true) {
                playlistExist = true
                break
            }
        }
        val playlistEntity: PlaylistEntity? = dbConverter.map(playlist)
        if (playlistEntity != null && !playlistExist) {
            appDatabase.playlistsDao().insertPlaylists(listOf(playlistEntity))
            emit(0)
        } else {
            appDatabase.playlistsDao().insertPlaylists(emptyList())
            emit(-1)
        }
    }

    override fun updatePlaylistRow(playlist: Playlist, trackId: Int, remove: Boolean): Flow<Int> = flow {
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
                    if (remove) {
                        tracks -= trackId
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
                }
                break
            }
        }
    }

    override fun mapPlaylistToString(playlist: Playlist): String {
        return dbConverter.mapPlaylistToString(playlist)
    }

    override fun mapStringToPlaylist(playlistString: String): Playlist {
        return dbConverter.mapStringToPlaylist(playlistString)
    }

    override fun getPlaylistRow(playlistId: Int): Flow<Playlist?> = flow {
        emit(dbConverter.map(appDatabase.playlistsDao().getPlaylistRow(playlistId)))
    }

    override fun getPlaylistId(playlist: Playlist): Flow<Int> = flow {
        val playlistsEntity = appDatabase.playlistsDao().selectAllPlaylists()
        if (playlistsEntity != null) {
            for (_playlistEntity in playlistsEntity) {
                if (playlist == dbConverter.map(_playlistEntity)) {
                    emit(_playlistEntity.playlistId)
                }
            }
        }
        emit(-1)
    }

    override fun deletePlaylist(playlist: Playlist): Flow<Int> = flow {
        val playlists: List<PlaylistEntity>? = appDatabase.playlistsDao().selectAllPlaylists()
        if (!playlists.isNullOrEmpty()) {
            for (_playlist in playlists) {
                if (dbConverter.map(_playlist)?.equals(playlist) == true) {
                    appDatabase.playlistsDao().deletePlaylistRow(_playlist.playlistId)
                    emit(_playlist.playlistId)
                }
            }
        }
        emit(-1)
    }

    override fun updatePlaylist(playlistOld: Playlist, playlistTitle :String, playlistDescription: String, playlistCoverLocalUri: String): Flow<Int> = flow {
        val playlists: List<PlaylistEntity>? = appDatabase.playlistsDao().selectAllPlaylists()
        if (!playlists.isNullOrEmpty()) {
            var canBeChanged = true
            var playlistIdForChange = -1
            for (_playlist in playlists) {
                if (dbConverter.map(_playlist)?.equals(playlistOld) == true) {
                    playlistIdForChange = _playlist.playlistId
                }
                if (_playlist.playlistTitle.equals(playlistTitle)
                    && _playlist.playlistDescription.equals(playlistDescription)
                    && _playlist.playlistCoverLocalUri.equals(playlistCoverLocalUri)) {
                    canBeChanged = false
                }
            }
            if (canBeChanged) {
                appDatabase.playlistsDao().updatePlaylist(
                    playlistCoverLocalUri,
                    playlistTitle,
                    playlistDescription,
                    playlistIdForChange
                )
                emit(playlistIdForChange)
            }
        }
        emit(-1)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>?): List<Playlist>? {
        return playlists?.map { playlist -> dbConverter.map(playlist)!! }
    }
}