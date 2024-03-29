package com.vaiwork.playlistmaker.domain.api

import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<ArrayList<Track>?, String?>>
}