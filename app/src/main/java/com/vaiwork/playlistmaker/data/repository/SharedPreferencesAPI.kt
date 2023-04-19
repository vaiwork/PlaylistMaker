package com.vaiwork.playlistmaker.data.repository

import com.vaiwork.playlistmaker.domain.entities.Track

interface SharedPreferencesAPI {
    fun getBooleanKeySharedPref(sharedPreferenceName: String,
                                sharedPreferenceMode: Int,
                                sharedPreferenceKey: String,
                                defaultValue: Boolean): Boolean

    fun getTracksSharedPref(sharedPreferenceName: String,
                            sharedPreferenceMode: Int,
                            sharedPreferenceKey: String): ArrayList<Track>

    fun setBooleanKeySharedPref(sharedPreferenceName: String,
                                sharedPreferenceMode: Int,
                                sharedPreferenceKey: String,
                                defaultValue: Boolean)
}