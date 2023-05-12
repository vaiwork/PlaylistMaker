package com.vaiwork.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
    companion object {
        const val DARK_MODE = "dark_mode"
        const val SETTINGS = "settings"
        const val HISTORY_TRACKS = "history_tracks"
    }
}