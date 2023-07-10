package com.vaiwork.playlistmaker.domain.api

import com.vaiwork.playlistmaker.util.Resource
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>>
}