package com.vaiwork.playlistmaker.data.db

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vaiwork.playlistmaker.data.DBClient
import com.vaiwork.playlistmaker.data.dto.TrackDto

class SharedPreferencesClient(
    private val sharedPreference: SharedPreferences,
    private val gson: Gson
) : DBClient {

    override fun getBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean {
        return sharedPreference
            .getBoolean(sharedPreferenceKey, defaultValue)
    }

    override fun getTracks(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String
    ): ArrayList<TrackDto>? {
        return gson.fromJson(
            sharedPreference.getString(sharedPreferenceKey, "[]"),
            object : TypeToken<List<TrackDto>>() {}.type
        )
    }

    override fun setBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        value: Boolean
    ) {
        sharedPreference
            .edit()
            .putBoolean(sharedPreferenceKey, value)
            .apply()
    }

    override fun putString(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: String
    ) {
        sharedPreference
            .edit()
            .putString(sharedPreferenceKey, defaultValue)
            .apply()
    }


}