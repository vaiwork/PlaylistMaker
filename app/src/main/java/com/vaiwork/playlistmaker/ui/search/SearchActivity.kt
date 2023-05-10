package com.vaiwork.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vaiwork.playlistmaker.Creator
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.audioplayer.AudioPleerActivity

class SearchActivity : AppCompatActivity() {

    private var tracksAdapter : TrackAdapter = TrackAdapter()
    private val tracksSearchController = Creator.provideTracksSearchController(this, tracksAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracksAdapter.setItemClickListener {
            if (tracksSearchController.clickDebounce()) {
                tracksSearchController.onDebounce(it)

                val audioPleerActivityIntent = Intent(this, AudioPleerActivity::class.java)
                startActivity(audioPleerActivityIntent)
            }
        }
        setContentView(R.layout.activity_search)
        tracksSearchController.onCreate(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksSearchController.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tracksSearchController.onRestoreInstanceState(savedInstanceState)
    }
}