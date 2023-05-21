package com.vaiwork.playlistmaker.ui.main.view_model

import android.app.Application
import android.content.res.Configuration
import android.provider.Settings.System.getConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vaiwork.playlistmaker.ui.search.activity.TracksState
import com.vaiwork.playlistmaker.ui.search.view_model.ToastState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(getApplication<Application>())

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    fun switchTheme() {
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