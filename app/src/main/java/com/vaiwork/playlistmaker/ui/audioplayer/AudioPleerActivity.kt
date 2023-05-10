package com.vaiwork.playlistmaker.ui.audioplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vaiwork.playlistmaker.Creator
import com.vaiwork.playlistmaker.R

class AudioPleerActivity : AppCompatActivity() {
    private val tracksMediaPlayerController = Creator.provideTracksMediaPlayerController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pleer)

        tracksMediaPlayerController.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        tracksMediaPlayerController.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksMediaPlayerController.onSaveInstanceState()
    }
}