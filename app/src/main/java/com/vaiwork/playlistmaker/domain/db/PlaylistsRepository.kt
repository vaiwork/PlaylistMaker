package com.vaiwork.playlistmaker.domain.db

import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getPlaylists(): Flow<List<Playlist>?>

    suspend fun addPlaylist(playlist: Playlist)

    fun updatePlaylistRow(playlist: Playlist, trackId: Int): Flow<Int>
}