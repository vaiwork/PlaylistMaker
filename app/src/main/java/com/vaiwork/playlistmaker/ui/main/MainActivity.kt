package com.vaiwork.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.presentation.main.MainView
import com.vaiwork.playlistmaker.ui.media.MediaActivity
import com.vaiwork.playlistmaker.ui.search.SearchActivity
import com.vaiwork.playlistmaker.ui.settings.SettingsActivity
import com.vaiwork.playlistmaker.util.Creator

class MainActivity : AppCompatActivity(), MainView {

    private val mainPresenter = Creator.provideMainPresenter(this, this)

    private lateinit var buttonSearch: Button
    private lateinit var buttonMedia: Button
    private lateinit var buttonSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSearch = findViewById(R.id.button_search)
        buttonMedia = findViewById(R.id.button_media)
        buttonSettings = findViewById(R.id.button_settings)

        mainPresenter.setCurrentAppTheme()

        buttonSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}