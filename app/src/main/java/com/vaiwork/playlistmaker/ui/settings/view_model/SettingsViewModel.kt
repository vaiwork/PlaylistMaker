package com.vaiwork.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {


    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(getApplication<Application>())

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
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