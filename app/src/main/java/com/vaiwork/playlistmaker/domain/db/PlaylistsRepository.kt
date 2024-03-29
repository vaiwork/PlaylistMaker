package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity
import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getPlaylists(): Flow<List<Playlist>?>

    fun addPlaylist(playlist: Playlist): Flow<Int>

    fun updatePlaylistRow(playlist: Playlist, trackId: Int, remove: Boolean = false): Flow<Int>

    fun deletePlaylist(playlist: Playlist): Flow<Int>

    fun updatePlaylist(playlistOld: Playlist, playlistTitle :String, playlistDescription: String, playlistCoverLocalUri: String): Flow<Int>

    fun mapPlaylistToString(playlist: Playlist): String

    fun mapStringToPlaylist(playlistString: String) : Playlist

    fun getPlaylistRow(playlistId: Int): Flow<Playlist?>

    fun getPlaylistId(playlist: Playlist): Flow<Int>
}