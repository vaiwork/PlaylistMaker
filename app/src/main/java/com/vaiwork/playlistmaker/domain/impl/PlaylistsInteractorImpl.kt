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

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override fun updatePlaylistRow(playlist: Playlist, trackId: Int): Flow<Int> {
        return playlistsRepository.updatePlaylistRow(playlist, trackId)
    }

    override fun mapPlaylistToString(playlist: Playlist): String {
        return playlistsRepository.mapPlaylistToString(playlist)
    }

    override fun mapStringToPlaylist(playlistString: String): Playlist {
        return playlistsRepository.mapStringToPlaylist(playlistString)
    }
}