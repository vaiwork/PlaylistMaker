package com.vaiwork.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vaiwork.playlistmaker.util.Creator
import com.vaiwork.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private val settingsController = Creator.provideSettingsController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsController.onCreate()
    }
}