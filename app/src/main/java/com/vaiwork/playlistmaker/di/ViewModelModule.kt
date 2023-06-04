package com.vaiwork.playlistmaker.di

import com.vaiwork.playlistmaker.ui.audioplayer.view_model.TracksMediaPlayerViewModel
import com.vaiwork.playlistmaker.ui.main.view_model.MainViewModel
import com.vaiwork.playlistmaker.ui.search.view_model.TracksSearchViewModel
import com.vaiwork.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), androidApplication())
    }

    viewModel {
        SettingsViewModel(get(), androidApplication())
    }

    viewModel {
        TracksSearchViewModel(get(), get(), androidApplication())
    }

    viewModel {
        TracksMediaPlayerViewModel(get(), get(), androidApplication())
    }
}