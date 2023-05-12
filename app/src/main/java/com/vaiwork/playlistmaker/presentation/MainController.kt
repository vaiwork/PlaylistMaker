package com.vaiwork.playlistmaker.presentation

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.media.MediaActivity
import com.vaiwork.playlistmaker.ui.search.SearchActivity
import com.vaiwork.playlistmaker.ui.settings.SettingsActivity
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class MainController(
    private val activity: Activity
) {
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(activity)

    private lateinit var buttonSearch: Button
    private lateinit var buttonMedia: Button
    private lateinit var buttonSettings: Button

    fun onCreate() {
        buttonSearch = activity.findViewById(R.id.button_search)
        buttonMedia = activity.findViewById(R.id.button_media)
        buttonSettings = activity.findViewById(R.id.button_settings)

        val currentTheme: Boolean = sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)
        sharedPreferenceInteractor.switchTheme(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, currentTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        buttonSearch.setOnClickListener {
            val searchIntent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(searchIntent)
        }

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(activity, MediaActivity::class.java)
            activity.startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(settingsIntent)
        }
    }
}