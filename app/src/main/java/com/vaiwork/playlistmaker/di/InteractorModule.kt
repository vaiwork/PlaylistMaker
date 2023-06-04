package com.vaiwork.playlistmaker.di

import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.domain.impl.SharedPreferenceInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksMediaPlayerInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SharedPreferenceInteractor> {
        SharedPreferenceInteractorImpl(get())
    }

    single<TracksInteractor>{
        TracksInteractorImpl(get())
    }

    single<TracksMediaPlayerInteractor> {
        TracksMediaPlayerInteractorImpl(get())
    }
}