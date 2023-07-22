package com.vaiwork.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.vaiwork.playlistmaker.data.DBClient
import com.vaiwork.playlistmaker.data.FavouriteTracksRepositoryImpl
import com.vaiwork.playlistmaker.data.MediaPlayerClient
import com.vaiwork.playlistmaker.data.NetworkClient
import com.vaiwork.playlistmaker.data.TrackDbConverter
import com.vaiwork.playlistmaker.data.db.AppDatabase
import com.vaiwork.playlistmaker.data.db.SharedPreferencesClient
import com.vaiwork.playlistmaker.data.media.TracksMediaPlayerClient
import com.vaiwork.playlistmaker.data.network.RetrofitNetworkClient
import com.vaiwork.playlistmaker.data.network.iTunesSearchApi
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksInteractor
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksRepository
import com.vaiwork.playlistmaker.domain.impl.FavouriteTracksInteractorImpl
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

    factory { TrackDbConverter() }

    single<MediaPlayerClient> {
        TracksMediaPlayerClient(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<DBClient> {
        SharedPreferencesClient(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}