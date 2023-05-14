package com.vaiwork.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.vaiwork.playlistmaker.presentation.main.MainPresenter
import com.vaiwork.playlistmaker.presentation.settings.SettingsPresenter
import com.vaiwork.playlistmaker.presentation.tracksmediaplayer.TracksMediaPlayerPresenter
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchPresenter

class App: Application() {
    companion object {
        const val DARK_MODE = "dark_mode"
        const val SETTINGS = "settings"
        const val HISTORY_TRACKS = "history_tracks"
    }

    var tracksSearchPresenter: TracksSearchPresenter? = null
    var settingsPresenter: SettingsPresenter? = null
    var mainPresenter: MainPresenter? = null
    var tracksMediaPlayerPresenter: TracksMediaPlayerPresenter? = null
}