package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import com.vaiwork.playlistmaker.domain.db.PlaylistsRepository
import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
): PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>?> {
        return playlistsRepository.getPlaylists()
    }

    override fun addPlaylist(playlist: Playlist): Flow<Int> {
        return playlistsRepository.addPlaylist(playlist)
    }

    override fun updatePlaylistRow(playlist: Playlist, trackId: Int, remove: Boolean): Flow<Int> {
        return playlistsRepository.updatePlaylistRow(playlist, trackId, remove)
    }

    override fun mapPlaylistToString(playlist: Playlist): String {
        return playlistsRepository.mapPlaylistToString(playlist)
    }

    override fun mapStringToPlaylist(playlistString: String): Playlist {
        return playlistsRepository.mapStringToPlaylist(playlistString)
    }

    override fun getPlaylistRow(playlistId: Int): Flow<Playlist?> {
        return playlistsRepository.getPlaylistRow(playlistId)
    }

    override fun getPlaylistId(playlist: Playlist): Flow<Int> {
        return playlistsRepository.getPlaylistId(playlist)
    }

    override fun deletePlaylist(playlist: Playlist): Flow<Int> {
        return playlistsRepository.deletePlaylist(playlist)
    }

    override fun updatePlaylist(playlistOld: Playlist, playlistTitle :String, playlistDescription: String, playlistCoverLocalUri: String): Flow<Int> {
        return playlistsRepository.updatePlaylist(playlistOld, playlistTitle, playlistDescription, playlistCoverLocalUri)
    }
}