package com.vaiwork.playlistmaker.ui.root.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.util.App

class RootViewModel(
    private val sharedPreferenceInteractor: SharedPreferenceInteractor,
    application: Application
) : AndroidViewModel(application) {

    fun switchTheme() {
        val currentTheme: Boolean = sharedPreferenceInteractor.getBoolean(
            App.SETTINGS,
            AppCompatActivity.MODE_PRIVATE, App.DARK_MODE, false
        )
        sharedPreferenceInteractor.switchTheme(
            App.SETTINGS,
            AppCompatActivity.MODE_PRIVATE, App.DARK_MODE, currentTheme
        )
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}