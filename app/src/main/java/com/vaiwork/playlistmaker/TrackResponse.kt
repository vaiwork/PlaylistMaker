package com.vaiwork.playlistmaker

import com.vaiwork.playlistmaker.domain.entities.Track

data class TrackResponse (
    val resultCount: Int,
    val results: ArrayList<Track>
)