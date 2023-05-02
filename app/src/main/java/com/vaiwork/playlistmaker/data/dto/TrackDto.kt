package com.vaiwork.playlistmaker.data.dto

data class TrackDto (val trackId: Int,
                  val trackName: String,
                  val artistName: String,
                  var trackTime: Long,
                  val artworkUrl100: String,
                  val collectionName: String,
                  val releaseDate: String,
                  val primaryGenreName: String,
                  val country: String,
                  val previewUrl: String
)