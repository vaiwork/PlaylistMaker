package com.vaiwork.playlistmaker.ui.audioplayer

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.presentation.tracksmediaplayer.TracksMediaPlayerPresenter
import com.vaiwork.playlistmaker.presentation.tracksmediaplayer.TracksMediaPlayerView
import com.vaiwork.playlistmaker.ui.search.TracksState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class AudioPlayerActivity : AppCompatActivity(), TracksMediaPlayerView {
    private var tracksMediaPlayerPresenter: TracksMediaPlayerPresenter? = null //Creator.provideTracksMediaPlayerPresenter(this, this)

    private lateinit var albumImageView: ImageView
    private lateinit var timeTextView: TextView
    private lateinit var albumTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var styleTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var playImageView: ImageView
    private lateinit var spendTimeTextView: TextView

    override fun onStart() {
        super.onStart()
        tracksMediaPlayerPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        tracksMediaPlayerPresenter?.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pleer)

        tracksMediaPlayerPresenter = (this.applicationContext as? App)?.tracksMediaPlayerPresenter
        if (tracksMediaPlayerPresenter == null) {
            tracksMediaPlayerPresenter = Creator.provideTracksMediaPlayerPresenter(this)
            (this.applicationContext as? App)?.tracksMediaPlayerPresenter = tracksMediaPlayerPresenter
        }
        tracksMediaPlayerPresenter?.attachView(this)

        toolbar = findViewById(R.id.activity_pleer_back_toolbar)
        playImageView = findViewById(R.id.activity_pleer_play_image_view)
        albumImageView = findViewById(R.id.activity_pleer_album_image)
        trackName = findViewById(R.id.activity_pleer_track_name)
        artistName = findViewById(R.id.activity_pleer_artist_name)
        timeTextView = findViewById(R.id.activity_pleer_time)
        albumTextView = findViewById(R.id.activity_pleer_album)
        yearTextView = findViewById(R.id.activity_pleer_year)
        styleTextView = findViewById(R.id.activity_pleer_style)
        countryTextView = findViewById(R.id.activity_pleer_country)
        spendTimeTextView = findViewById(R.id.activity_pleer_spend_time_text_view)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        tracksMediaPlayerPresenter?.setDefaultPlayerState()
        tracksMediaPlayerPresenter?.preparePlayer()
        tracksMediaPlayerPresenter?.setAlbumImage(R.drawable.placeholder_album_image_light_mode,
            R.dimen.activity_pleer_album_image_corner_radius,
            albumImageView)

        trackName.text = tracksMediaPlayerPresenter?.getTrackName()
        artistName.text = tracksMediaPlayerPresenter?.getTrackArtistName()
        timeTextView.text = tracksMediaPlayerPresenter?.getTrackTime()
        albumTextView.text = tracksMediaPlayerPresenter?.getTrackCollection()
        yearTextView.text = tracksMediaPlayerPresenter?.getTrackYear()
        styleTextView.text = tracksMediaPlayerPresenter?.getTrackPrimaryGenre()
        countryTextView.text = tracksMediaPlayerPresenter?.getTrackCountry()

        playImageView.setOnClickListener {
            tracksMediaPlayerPresenter?.playbackControl()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tracksMediaPlayerPresenter?.detachView()
        tracksMediaPlayerPresenter?.onDestroy()

        if (isFinishing()) {
            // Очищаем ссылку на Presenter в Application
            (this.application as? App)?.tracksMediaPlayerPresenter = null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksMediaPlayerPresenter?.detachView()
        tracksMediaPlayerPresenter?.onSaveInstanceState()
    }

    override fun onPause() {
        super.onPause()
        tracksMediaPlayerPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        tracksMediaPlayerPresenter?.detachView()
    }

    override fun setSpendTime(text: String) {
        spendTimeTextView.text = text
    }

    override fun setNightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_night_mode_button)
    }

    override fun setLightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_light_mode_button)
    }

    override fun setLightPlayImage() {
        playImageView.setImageResource(R.drawable.play_light_mode_button)
    }

    override fun activatePlayImage(value: Boolean) {
        playImageView.isEnabled = value
    }
}