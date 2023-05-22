package com.vaiwork.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.vaiwork.playlistmaker.data.DBClient
import com.vaiwork.playlistmaker.data.MediaPlayerClient
import com.vaiwork.playlistmaker.data.NetworkClient
import com.vaiwork.playlistmaker.data.db.SharedPreferencesClient
import com.vaiwork.playlistmaker.data.media.TracksMediaPlayerClient
import com.vaiwork.playlistmaker.data.network.RetrofitNetworkClient
import com.vaiwork.playlistmaker.data.network.iTunesSearchApi
import com.vaiwork.playlistmaker.util.App
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<iTunesSearchApi> {
        Retrofit.Builder()
        .baseUrl("http://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(iTunesSearchApi::class.java)
    }

    single {
        androidContext().getSharedPreferences(App.SETTINGS, Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }

    factory {
        MediaPlayer()
    }

    single<MediaPlayerClient> {
        TracksMediaPlayerClient(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<DBClient> {
        SharedPreferencesClient(get(), get())
    }
}