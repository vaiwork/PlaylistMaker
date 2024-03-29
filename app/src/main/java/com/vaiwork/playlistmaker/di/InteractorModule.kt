package com.vaiwork.playlistmaker.di

import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksInteractor
import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackInteractor
import com.vaiwork.playlistmaker.domain.impl.FavouriteTracksInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.PlaylistsInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.PlaylistsTrackInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.SharedPreferenceInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksInteractorImpl
import com.vaiwork.playlistmaker.domain.impl.TracksMediaPlayerInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SharedPreferenceInteractor> {
        SharedPreferenceInteractorImpl(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<TracksMediaPlayerInteractor> {
        TracksMediaPlayerInteractorImpl(get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(get())
    }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

    single<PlaylistsTrackInteractor> {
        PlaylistsTrackInteractorImpl(get())
    }
}