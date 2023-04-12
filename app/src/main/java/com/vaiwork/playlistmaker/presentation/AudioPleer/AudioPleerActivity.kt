package com.vaiwork.playlistmaker.presentation.AudioPleer

import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

import java.text.SimpleDateFormat

import java.util.*

import kotlin.collections.ArrayList

import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.vaiwork.playlistmaker.App
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.SearchHistory
import com.vaiwork.playlistmaker.Track
import com.vaiwork.playlistmaker.data.MediaPlayerInteraction
import com.vaiwork.playlistmaker.data.Utils

class AudioPleerActivity : AppCompatActivity(), MediaPlayerInteraction {

    private val mediaPlayer = com.vaiwork.playlistmaker.data.MediaPlayer()

    private val audioPleerHandler = Handler(Looper.getMainLooper())
    private val audioPleerRunnable = Runnable {
        spendTimeControl()
    }

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
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val tracks: ArrayList<Track> = SearchHistory((applicationContext as App).sharedPrefs)
            .getItemsFromSharedPrefs()!!
        val clickedTrack = tracks[0]

        playImageView = findViewById(R.id.activity_pleer_play_image_view)
        preparePlayer(clickedTrack)

        albumImageView = findViewById(R.id.activity_pleer_album_image)

        Utils().setImageWithPlaceholder(this, albumImageView,
            clickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
            R.drawable.placeholder_album_image_light_mode,
            RequestOptions.bitmapTransform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.activity_pleer_album_image_corner_radius)
                )
            )
        )

        trackName = findViewById(R.id.activity_pleer_track_name)
        trackName.text = clickedTrack.trackName

        artistName = findViewById(R.id.activity_pleer_artist_name)
        artistName.text = clickedTrack.artistName

        timeTextView = findViewById(R.id.activity_pleer_time)
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(clickedTrack.trackTime)

        albumTextView = findViewById(R.id.activity_pleer_album)
        albumTextView.text = clickedTrack.collectionName

        yearTextView = findViewById(R.id.activity_pleer_year)
        yearTextView.text = clickedTrack.releaseDate.subSequence(0,4)

        styleTextView = findViewById(R.id.activity_pleer_style)
        styleTextView.text = clickedTrack.primaryGenreName

        countryTextView = findViewById(R.id.activity_pleer_country)
        countryTextView.text = clickedTrack.country

        spendTimeTextView = findViewById(R.id.activity_pleer_spend_time_text_view)

        playImageView.setOnClickListener {
            playbackControl()
        }
    }

    override fun startPlayer() {
        mediaPlayer.mediaPlayer.start()

        if (Utils().getBooleanKeySharedPref(applicationContext as App,
                App.SETTINGS,
                MODE_PRIVATE,
                App.DARK_MODE,
                false)) {
            playImageView.setImageResource(R.drawable.pause_night_mode_button)
        } else {
            playImageView.setImageResource(R.drawable.pause_light_mode_button)
        }

        mediaPlayer.playerState = com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PLAYING
        spendTimeControl()
    }

    override fun pausePlayer() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.mediaPlayer.pause()
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        mediaPlayer.playerState = com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PAUSED
    }

    override fun preparePlayer(clickedTrack: Track) {
        mediaPlayer.mediaPlayer.setDataSource(clickedTrack.previewUrl)
        mediaPlayer.mediaPlayer.prepareAsync()
        mediaPlayer.mediaPlayer.setOnPreparedListener {
            playImageView.isEnabled = true
            mediaPlayer.playerState = com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PREPARED
        }
        mediaPlayer.mediaPlayer.setOnCompletionListener {
            spendTimeTextView.text = "00:00"
            playImageView.setImageResource(R.drawable.play_light_mode_button)
            mediaPlayer.playerState = com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PREPARED
        }
    }

    override fun playbackControl() {
        when(mediaPlayer.playerState) {
            com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PLAYING -> {
                pausePlayer()
            }
            com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PREPARED,
            com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun spendTimeControl() {
        if (com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PLAYING == mediaPlayer.playerState) {
            audioPleerHandler.postDelayed(audioPleerRunnable,
                com.vaiwork.playlistmaker.data.MediaPlayer.AUDIO_PLEER_DELAY)
            spendTimeTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                    mediaPlayer.mediaPlayer.currentPosition
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.mediaPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        spendTimeTextView.text = "00:00"
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.mediaPlayer.stop()
        mediaPlayer.mediaPlayer.prepareAsync()
        mediaPlayer.playerState = com.vaiwork.playlistmaker.data.MediaPlayer.STATE_PREPARED
    }
}