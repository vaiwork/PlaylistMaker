package com.vaiwork.playlistmaker.domain.api

import com.vaiwork.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<ArrayList<Track>>
}