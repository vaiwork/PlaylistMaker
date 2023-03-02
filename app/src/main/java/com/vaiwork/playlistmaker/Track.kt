package com.vaiwork.playlistmaker

import com.google.gson.annotations.SerializedName
import java.util.*

data class Track (@SerializedName("trackId") val trackId: Int,
                  @SerializedName("trackName") val trackName: String,
                  @SerializedName("artistName") val artistName: String,
                  @SerializedName("trackTimeMillis") var trackTime: Long,
                  @SerializedName("artworkUrl100") val artworkUrl100: String)