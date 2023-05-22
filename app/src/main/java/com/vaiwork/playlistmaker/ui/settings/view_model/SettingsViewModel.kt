package com.vaiwork.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.util.App

class SettingsViewModel(
    private val sharedPreferenceInteractor: SharedPreferenceInteractor,
    application: Application
): AndroidViewModel(application) {

    fun getDarkModeValue(): Boolean {
        return sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)
    }

    fun switchAppTheme(checked: Boolean) {
        sharedPreferenceInteractor.switchTheme(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, checked)
        AppCompatDelegate.setDefaultNightMode(
            if (checked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}