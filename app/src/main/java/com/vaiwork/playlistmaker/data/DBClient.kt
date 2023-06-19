package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.dto.TrackDto

interface DBClient {
    fun getBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean

    fun getTracks(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String
    ): ArrayList<TrackDto>?

    fun setBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        value: Boolean
    )

    fun putString(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: String
    )
}