package com.vaiwork.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.vaiwork.playlistmaker.ui.media.MediaActivity
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.settings.SettingsActivity
import com.vaiwork.playlistmaker.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button Search anonymous class
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonSearchClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        // Button Media lambda
        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        // Button Settings lambda
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}