package com.vaiwork.playlistmaker.presentation.ui.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.vaiwork.playlistmaker.domain.usecases.SharedPreferencesAPIInteractor

class App: Application() {

    companion object {
        const val DARK_MODE = "dark_mode"
        const val SETTINGS = "settings"
        const val HISTORY_TRACKS = "history_tracks"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        darkTheme = SharedPreferencesAPIInteractor(this).getBooleanKeySharedPref(SETTINGS, MODE_PRIVATE, DARK_MODE, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        SharedPreferencesAPIInteractor(this).setBooleanKeySharedPref(SETTINGS, MODE_PRIVATE, DARK_MODE, darkThemeEnabled)
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