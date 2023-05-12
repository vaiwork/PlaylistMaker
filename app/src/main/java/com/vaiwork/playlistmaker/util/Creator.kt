package com.vaiwork.playlistmaker.util

import android.app.Activity
import android.content.Context
import com.vaiwork.playlistmaker.data.SharedPreferenceRepositoryImpl
import com.vaiwork.playlistmaker.data.TracksMediaPlayerRepositoryImpl
import com.vaiwork.playlistmaker.data.TracksRepositoryImpl
import com.vaiwork.playlistmaker.data.db.SharedPreferencesClient
import com.vaiwork.playlistmaker.data.media.TracksMediaPlayerClient
import com.vaiwork.playlistmaker.data.network.RetrofitNetworkClient
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceRepository
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerRepository
import com.vaiwork.playlistmaker.domain.api.TracksRepository
import com.vaiwork.playlistmaker.domain.impl.SharedPreferenceInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksMediaPlayerInteractorImpl
import com.vaiwork.playlistmaker.presentation.MainController
import com.vaiwork.playlistmaker.presentation.SettingsController
import com.vaiwork.playlistmaker.presentation.TracksMediaPlayerController
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchPresenter
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchView
import com.vaiwork.playlistmaker.ui.search.TrackAdapter

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideTracksSearchPresenter(tracksSearchView: TracksSearchView, context: Context): TracksSearchPresenter {
        return TracksSearchPresenter(tracksSearchView, context)
    }

    private fun getSharedPreferenceRepository(context: Context): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImpl(SharedPreferencesClient(context))
    }

    fun provideSharedPreferenceInteractor(context: Context): SharedPreferenceInteractor {
        return SharedPreferenceInteractorImpl(getSharedPreferenceRepository(context))
    }

    private fun getTracksMediaPlayerRepository(): TracksMediaPlayerRepository {
        return TracksMediaPlayerRepositoryImpl(TracksMediaPlayerClient())
    }

    fun provideTracksMediaPlayerInteractor(): TracksMediaPlayerInteractor {
        return TracksMediaPlayerInteractorImpl(getTracksMediaPlayerRepository())
    }

    fun provideTracksMediaPlayerController(activity: Activity): TracksMediaPlayerController {
        return TracksMediaPlayerController(activity)
    }

    fun provideSettingsController(activity: Activity): SettingsController {
        return SettingsController(activity)
    }

    fun provideMainController(activity: Activity): MainController {
        return MainController(activity)
    }
}