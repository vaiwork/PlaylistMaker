package com.vaiwork.playlistmaker.presentation.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.vaiwork.playlistmaker.presentation.settings.SettingsView
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class MainPresenter(
    private val context: Context
) {
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(context)

    private var view: MainView? = null

    fun attachView(view: MainView) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun setCurrentAppTheme() {
        val currentTheme: Boolean = sharedPreferenceInteractor.getBoolean(
            App.SETTINGS,
            AppCompatActivity.MODE_PRIVATE, App.DARK_MODE, false)
        sharedPreferenceInteractor.switchTheme(
            App.SETTINGS,
            AppCompatActivity.MODE_PRIVATE, App.DARK_MODE, currentTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}