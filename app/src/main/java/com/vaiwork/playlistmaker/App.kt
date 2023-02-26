package com.vaiwork.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class App: Application() {

    companion object {
        const val DARK_MODE = "dark_mode"
        const val SETTINGS = "settings"
        const val HISTORY_TRACKS = "history_tracks"
    }

    lateinit var sharedPrefs: SharedPreferences
    var darkTheme = false

    override fun onCreate() {
        sharedPrefs = getSharedPreferences(SETTINGS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_MODE, false)
        switchTheme(darkTheme)
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(DARK_MODE, darkThemeEnabled).apply()
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun addItemToSharedPrefs(itemTrack: Track) {
        var tracks = Gson().fromJson<ArrayList<Track>>(getSharedPreferences(SETTINGS, MODE_PRIVATE).getString(HISTORY_TRACKS, null), object : TypeToken<List<Track>>() {}.type)
        if (tracks == null) {
            tracks = ArrayList<Track>()
            tracks.add(itemTrack)
        } else {
            var cloneTrackIndex: Int = -1
            for(track: Track in tracks) {
                if (track.trackId == itemTrack.trackId) {
                    cloneTrackIndex = tracks.indexOf(track)
                    break
                }
            }
            if (cloneTrackIndex != -1) {
                tracks.removeAt(cloneTrackIndex)
                tracks.add(0,itemTrack)
            } else {
                tracks.add(0,itemTrack)
                if (tracks.size > 10) {
                    tracks.removeAt(10)
                }
            }
        }
        getSharedPreferences(SETTINGS, MODE_PRIVATE).edit().putString(HISTORY_TRACKS, Gson().toJson(tracks, object : TypeToken<List<Track>>() {}.type)).apply()
    }

    fun clearItemsFromSharedPrefs() {
        getSharedPreferences(SETTINGS, MODE_PRIVATE).edit().putString(HISTORY_TRACKS, "").apply()
    }

    fun getItemsFromSharedPrefs(): ArrayList<Track>? {
        return Gson().fromJson<ArrayList<Track>>(getSharedPreferences(SETTINGS, MODE_PRIVATE).getString(HISTORY_TRACKS, ""), object : TypeToken<List<Track>>() {}.type)
    }
}