package com.vaiwork.playlistmaker.data.network

import com.vaiwork.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchApi {

    @GET("search")
    suspend fun searchTracks(@Query("term") text: String): TrackSearchResponse

}