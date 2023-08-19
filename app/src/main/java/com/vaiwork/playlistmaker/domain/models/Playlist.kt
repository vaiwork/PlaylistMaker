package com.vaiwork.playlistmaker.domain.models

data class Playlist(
    val playlistTitle: String,
    val playlistDescription: String,
    val playlistCoverLocalUri: String,
    val playlistTracks: List<Int>,
    val playlistTracksNumber: Int
)