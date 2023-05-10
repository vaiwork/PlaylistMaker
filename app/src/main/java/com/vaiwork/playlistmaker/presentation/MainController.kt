package com.vaiwork.playlistmaker.presentation

import android.app.Activity
import android.content.Intent
import android.widget.Button
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.media.MediaActivity
import com.vaiwork.playlistmaker.ui.search.SearchActivity
import com.vaiwork.playlistmaker.ui.settings.SettingsActivity

class MainController(
    private val activity: Activity
) {
    private lateinit var buttonSearch: Button
    private lateinit var buttonMedia: Button
    private lateinit var buttonSettings: Button

    fun onCreate() {
        buttonSearch = activity.findViewById(R.id.button_search)
        buttonMedia = activity.findViewById(R.id.button_media)
        buttonSettings = activity.findViewById(R.id.button_settings)

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