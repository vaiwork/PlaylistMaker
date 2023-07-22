package com.vaiwork.playlistmaker.di

import com.vaiwork.playlistmaker.data.FavouriteTracksRepositoryImpl
import com.vaiwork.playlistmaker.data.SharedPreferenceRepositoryImpl
import com.vaiwork.playlistmaker.data.TracksMediaPlayerRepositoryImpl
import com.vaiwork.playlistmaker.data.TracksRepositoryImpl
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceRepository
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerRepository
import com.vaiwork.playlistmaker.domain.api.TracksRepository
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<TracksMediaPlayerRepository> {
        TracksMediaPlayerRepositoryImpl(get())
    }

    single<SharedPreferenceRepository> {
        SharedPreferenceRepositoryImpl(get())
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get(), get())
    }
}