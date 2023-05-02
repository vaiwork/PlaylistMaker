package com.vaiwork.playlistmaker.data.dto

data class TrackSearchResponse (
    val searchType: String,
    val expression: String,
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()