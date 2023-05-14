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
import com.vaiwork.playlistmaker.presentation.main.MainPresenter
import com.vaiwork.playlistmaker.presentation.settings.SettingsPresenter
import com.vaiwork.playlistmaker.presentation.main.MainView
import com.vaiwork.playlistmaker.presentation.settings.SettingsView
import com.vaiwork.playlistmaker.presentation.tracksmediaplayer.TracksMediaPlayerPresenter
import com.vaiwork.playlistmaker.presentation.tracksmediaplayer.TracksMediaPlayerView
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchPresenter
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchView

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

    fun provideTracksMediaPlayerPresenter(tracksMediaPlayerView: TracksMediaPlayerView, context: Context): TracksMediaPlayerPresenter {
        return TracksMediaPlayerPresenter(tracksMediaPlayerView, context)
    }

    fun provideSettingsController(view: SettingsView, context: Context): SettingsPresenter {
        return SettingsPresenter(view, context)
    }

    fun provideMainPresenter(view: MainView, context: Context): MainPresenter {
        return MainPresenter(view, context)
    }
}