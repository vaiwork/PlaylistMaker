package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackInteractor
import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackRepository
import com.vaiwork.playlistmaker.domain.models.Track

class PlaylistsTrackInteractorImpl(
    private var playlistsTrackRepository: PlaylistsTrackRepository
): PlaylistsTrackInteractor {
    override suspend fun insertTrackToPlaylist(track: Track) {
        return playlistsTrackRepository.insertTrackToPlaylist(track)
    }
}