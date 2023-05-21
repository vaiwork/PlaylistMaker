package com.vaiwork.playlistmaker.domain.api

import com.vaiwork.playlistmaker.domain.models.Track

interface SharedPreferenceInteractor {
    fun addTrack(dto: Track, sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String)

    fun getTracks(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String): ArrayList<Track>

    fun clear(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String)

    fun getBoolean(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String, defaultValue: Boolean): Boolean

    fun switchTheme(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String, value: Boolean)
}