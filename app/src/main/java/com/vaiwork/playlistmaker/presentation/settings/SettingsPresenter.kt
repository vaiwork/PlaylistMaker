package com.vaiwork.playlistmaker.presentation.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class SettingsPresenter(
    private val context: Context
) {


    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(context)

    private var view: SettingsView? = null

    fun attachView(view: SettingsView) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

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