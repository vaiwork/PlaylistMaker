package com.vaiwork.playlistmaker.domain.usecases

import android.content.Context
import com.vaiwork.playlistmaker.data.SharedPreferencesAPIImp
import com.vaiwork.playlistmaker.domain.entities.Track

class SharedPreferencesAPIInteractor (context: Context) {

    private val sharedPreferencesAPIImp: SharedPreferencesAPIImp = SharedPreferencesAPIImp(context)

    fun getBooleanKeySharedPref(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String, defaultValue: Boolean): Boolean {
        return sharedPreferencesAPIImp.getBooleanKeySharedPref(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, defaultValue)
    }

    fun getTracksSharedPref(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String ): ArrayList<Track> {
        return sharedPreferencesAPIImp.getTracksSharedPref(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
    }

    fun setBooleanKeySharedPref(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String, defaultValue: Boolean) {
        sharedPreferencesAPIImp.setBooleanKeySharedPref(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, defaultValue)
    }
}