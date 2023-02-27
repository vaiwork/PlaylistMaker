package com.vaiwork.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

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
}