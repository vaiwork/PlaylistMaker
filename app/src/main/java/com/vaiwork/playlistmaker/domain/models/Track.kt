package com.vaiwork.playlistmaker.domain.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Track (@SerializedName("trackId") val trackId: Int,
                  @SerializedName("trackName") val trackName: String,
                  @SerializedName("artistName") val artistName: String,
                  @SerializedName("trackTimeMillis") var trackTime: Long,
                  @SerializedName("artworkUrl100") val artworkUrl100: String,
                  @SerializedName("collectionName") val collectionName: String,
                  @SerializedName("releaseDate") val releaseDate: String,
                  @SerializedName("primaryGenreName") val primaryGenreName: String,
                  @SerializedName("country") val country: String,
                  @SerializedName("previewUrl") val previewUrl: String
)