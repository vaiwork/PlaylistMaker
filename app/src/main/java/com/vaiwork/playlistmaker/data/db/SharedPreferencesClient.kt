package com.vaiwork.playlistmaker.data.db

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vaiwork.playlistmaker.data.DBClient
import com.vaiwork.playlistmaker.data.dto.TrackDto

class SharedPreferencesClient(
    private val context: Context
): DBClient {

    override fun getBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean {
        return context
            .getSharedPreferences(sharedPreferenceName, sharedPreferenceMode)
            .getBoolean(sharedPreferenceKey, defaultValue)
    }

    override fun getTracks(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String
    ): ArrayList<TrackDto>? {
        return Gson().fromJson(context.getSharedPreferences(sharedPreferenceName, sharedPreferenceMode).getString(sharedPreferenceKey, "[]"), object : TypeToken<List<TrackDto>>() {}.type)
    }

    override fun setBooleanKey(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        value: Boolean
    ) {
        context
            .getSharedPreferences(sharedPreferenceName, sharedPreferenceMode)
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
        context
            .getSharedPreferences(sharedPreferenceName, sharedPreferenceMode)
            .edit()
            .putString(sharedPreferenceKey, defaultValue)
            .apply()
    }


}