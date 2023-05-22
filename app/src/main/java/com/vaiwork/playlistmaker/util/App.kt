package com.vaiwork.playlistmaker.util

import android.app.Application
import com.vaiwork.playlistmaker.di.dataModule
import com.vaiwork.playlistmaker.di.interactorModule
import com.vaiwork.playlistmaker.di.repositoryModule
import com.vaiwork.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    companion object {
        const val DARK_MODE = "dark_mode"
        const val SETTINGS = "settings"
        const val HISTORY_TRACKS = "history_tracks"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}