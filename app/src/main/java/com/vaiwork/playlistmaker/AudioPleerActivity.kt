package com.vaiwork.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AudioPleerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val AUDIO_PLEER_DELAY = 500L
    }

    lateinit var sharedPrefs: SharedPreferences

    private var mediaPlayer = MediaPlayer()

    private val audioPleerHandler = Handler(Looper.getMainLooper())
    private val audioPleerRunnable = Runnable {
        spendTimeControl()
    }

    private var playerState = STATE_DEFAULT

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

        sharedPrefs = getSharedPreferences(App.SETTINGS, MODE_PRIVATE)

        toolbar = findViewById(R.id.activity_pleer_back_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val tracks: ArrayList<Track> = SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs()!!
        val clickedTrack = tracks[0]

        playImageView = findViewById(R.id.activity_pleer_play_image_view)
        preparePlayer(clickedTrack)

        albumImageView = findViewById(R.id.activity_pleer_album_image)
        Glide.with(this).load(clickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_album_image_light_mode)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.activity_pleer_album_image_corner_radius))))
            .into(albumImageView)

        trackName = findViewById(R.id.activity_pleer_track_name)
        trackName.text = clickedTrack.trackName

        artistName = findViewById(R.id.activity_pleer_artist_name)
        artistName.text = clickedTrack.artistName

        timeTextView = findViewById(R.id.activity_pleer_time)
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(clickedTrack.trackTime)

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

    private fun startPlayer() {
        mediaPlayer.start()
        if (sharedPrefs.getBoolean(App.DARK_MODE, false)) {
            playImageView.setImageResource(R.drawable.pause_night_mode_button)
        } else {
            playImageView.setImageResource(R.drawable.pause_light_mode_button)
        }
        playerState = STATE_PLAYING
        spendTimeControl()
    }

    private fun pausePlayer() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.pause()
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        playerState = STATE_PAUSED
    }

    private fun preparePlayer(clickedTrack: Track) {
        mediaPlayer.setDataSource(clickedTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playImageView.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            spendTimeTextView.text = "00:00"
            playImageView.setImageResource(R.drawable.play_light_mode_button)
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun spendTimeControl() {
        if (STATE_PLAYING == playerState) {
            audioPleerHandler.postDelayed(audioPleerRunnable, AUDIO_PLEER_DELAY)
            spendTimeTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        spendTimeTextView.text = "00:00"
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        mediaPlayer.stop()
        mediaPlayer.prepareAsync()
        playerState = STATE_PREPARED
    }
}