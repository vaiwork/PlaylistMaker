package com.vaiwork.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.main.view_model.MainViewModel
import com.vaiwork.playlistmaker.ui.media.activity.MediaActivity
import com.vaiwork.playlistmaker.ui.search.activity.SearchActivity
import com.vaiwork.playlistmaker.ui.settings.activity.SettingsActivity
import com.vaiwork.playlistmaker.util.App

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var buttonSearch: Button
    private lateinit var buttonMedia: Button
    private lateinit var buttonSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this, MainViewModel.getViewModelFactory())[MainViewModel::class.java]

        buttonSearch = findViewById(R.id.button_search)
        buttonMedia = findViewById(R.id.button_media)
        buttonSettings = findViewById(R.id.button_settings)

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

        mainViewModel.switchTheme()
    }
}