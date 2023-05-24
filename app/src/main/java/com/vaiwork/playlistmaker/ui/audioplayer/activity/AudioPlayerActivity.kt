package com.vaiwork.playlistmaker.ui.audioplayer.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.ActivatePlayState
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.SpendTimeState
import com.vaiwork.playlistmaker.ui.audioplayer.view_model.TracksMediaPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {
    private val tracksMediaPlayerViewModel: TracksMediaPlayerViewModel by viewModel()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_pleer)

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

        tracksMediaPlayerViewModel.setDefaultPlayerState()
        tracksMediaPlayerViewModel.preparePlayer()
        tracksMediaPlayerViewModel.setAlbumImage(R.drawable.placeholder_album_image_light_mode,
            R.dimen.activity_pleer_album_image_corner_radius,
            albumImageView)

        trackName.text = tracksMediaPlayerViewModel.getTrackName()
        artistName.text = tracksMediaPlayerViewModel.getTrackArtistName()
        timeTextView.text = tracksMediaPlayerViewModel.getTrackTime()
        albumTextView.text = tracksMediaPlayerViewModel.getTrackCollection()
        yearTextView.text = tracksMediaPlayerViewModel.getTrackYear()
        styleTextView.text = tracksMediaPlayerViewModel.getTrackPrimaryGenre()
        countryTextView.text = tracksMediaPlayerViewModel.getTrackCountry()

        playImageView.setOnClickListener {
            tracksMediaPlayerViewModel.playbackControl()
        }

        tracksMediaPlayerViewModel.observeAudioPlayerState().observe(this) {
            render(it)
        }

        tracksMediaPlayerViewModel.observeSetSpendTime().observe(this) {spendTimeState ->
            if (spendTimeState is SpendTimeState.Changed) {
                setSpendTime(spendTimeState.timeString)
                tracksMediaPlayerViewModel.spendTimeWasChanged()
            }
        }

        tracksMediaPlayerViewModel.observeActivatePlayImage().observe(this) { activatePlayImage ->
            if (activatePlayImage is ActivatePlayState.Changed) {
                activatePlayImage(activatePlayImage.isEnabled)
                tracksMediaPlayerViewModel.activatePlayImageChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        tracksMediaPlayerViewModel.onSaveInstanceState()
        super.onSaveInstanceState(outState)
    }

    private fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.PreparedPaused -> setLightPlayImage()
            is AudioPlayerState.Started -> setPauseImage(state.isDarkTheme)
        }
    }

    private fun setSpendTime(text: String) {
        spendTimeTextView.text = text
    }

    private fun setPauseImage(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            setNightPauseImage()
        } else {
            setLightPauseImage()
        }
    }

    private fun setNightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_night_mode_button)
    }

    private fun setLightPauseImage() {
        playImageView.setImageResource(R.drawable.pause_light_mode_button)
    }


    private fun setLightPlayImage() {
        playImageView.setImageResource(R.drawable.play_light_mode_button)
    }


    private fun activatePlayImage(value: Boolean) {
        playImageView.isEnabled = value
    }
}