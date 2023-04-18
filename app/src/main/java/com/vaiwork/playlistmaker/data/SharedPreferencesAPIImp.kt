package com.vaiwork.playlistmaker.data

import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vaiwork.playlistmaker.data.repository.SharedPreferencesAPI

import com.vaiwork.playlistmaker.domain.entities.Track

class SharedPreferencesAPIImp(
    private val context: Context
) : SharedPreferencesAPI {

    override fun getBooleanKeySharedPref(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean {
        return context
            .getSharedPreferences(sharedPreferenceName, sharedPreferenceMode)
            .getBoolean(sharedPreferenceKey, defaultValue)
    }

    override fun getTracksSharedPref(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String
    ): ArrayList<Track> {
        return Gson().fromJson<ArrayList<Track>>(context.getSharedPreferences(sharedPreferenceName, sharedPreferenceMode).getString(sharedPreferenceKey, ""), object : TypeToken<List<Track>>() {}.type)
    }

    override fun setBooleanKeySharedPref(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ) {
        context.getSharedPreferences(sharedPreferenceName, sharedPreferenceMode).edit().putBoolean(sharedPreferenceKey, defaultValue).apply()
    }
}