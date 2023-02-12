package com.vaiwork.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchApi {

    @GET("search")
    fun getTracks(@Query("term") text: String): Call<TrackResponse>

}