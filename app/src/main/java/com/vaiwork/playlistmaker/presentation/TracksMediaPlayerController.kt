package com.vaiwork.playlistmaker.presentation

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksMediaPlayerController(
    private val activity: Activity
) {
    private val tracksMediaPlayerInteractor = Creator.provideTracksMediaPlayerInteractor()
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(activity)

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

    fun onCreate() {
        toolbar = activity.findViewById(R.id.activity_pleer_back_toolbar)
        playImageView = activity.findViewById(R.id.activity_pleer_play_image_view)
        albumImageView = activity.findViewById(R.id.activity_pleer_album_image)
        trackName = activity.findViewById(R.id.activity_pleer_track_name)
        artistName = activity.findViewById(R.id.activity_pleer_artist_name)
        timeTextView = activity.findViewById(R.id.activity_pleer_time)
        albumTextView = activity.findViewById(R.id.activity_pleer_album)
        yearTextView = activity.findViewById(R.id.activity_pleer_year)
        styleTextView = activity.findViewById(R.id.activity_pleer_style)
        countryTextView = activity.findViewById(R.id.activity_pleer_country)
        spendTimeTextView = activity.findViewById(R.id.activity_pleer_spend_time_text_view)

        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStateDefault())

        toolbar.setNavigationOnClickListener { activity.onBackPressed() }
        val tracks = sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
        val clickedTrack = tracks[0]
        preparePlayer(clickedTrack)


        Glide.with(activity).load(clickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_album_image_light_mode)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(activity.resources.getDimensionPixelSize(R.dimen.activity_pleer_album_image_corner_radius))))
            .into(albumImageView)


        trackName.text = clickedTrack.trackName
        artistName.text = clickedTrack.artistName
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(clickedTrack.trackTime)
        albumTextView.text = clickedTrack.collectionName
        yearTextView.text = clickedTrack.releaseDate.subSequence(0,4)
        styleTextView.text = clickedTrack.primaryGenreName
        countryTextView.text = clickedTrack.country

        playImageView.setOnClickListener {
            playbackControl()
        }
    }


    private fun spendTimeControl() {
        if (tracksMediaPlayerInteractor.getPlayerState() == tracksMediaPlayerInteractor.getPlayerState()) {
            audioPleerHandler.postDelayed(audioPleerRunnable, tracksMediaPlayerInteractor.getAudioPleerDelay())
            spendTimeTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(tracksMediaPlayerInteractor.getCurrentPosition())
        }
    }

    private fun startPlayer() {
        tracksMediaPlayerInteractor.startPlayer()
        if (sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)) {
            playImageView.setImageResource(R.drawable.pause_night_mode_button)
        } else {
            playImageView.setImageResource(R.drawable.pause_light_mode_button)
        }
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePlaying())
        spendTimeControl()
    }

    private fun pausePlayer() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.pausePlayer()
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePaused())
    }

    private fun preparePlayer(clickedTrack: Track) {
        tracksMediaPlayerInteractor.setDataSource(clickedTrack.previewUrl)
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.onPreparedListener {
            playImageView.isEnabled = true
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
        tracksMediaPlayerInteractor.onCompletionListener {
            spendTimeTextView.text = "00:00"
            playImageView.setImageResource(R.drawable.play_light_mode_button)
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
    }

    private fun playbackControl() {
        when(tracksMediaPlayerInteractor.getPlayerState()) {
            tracksMediaPlayerInteractor.getStatePlaying() -> {
                pausePlayer()
            }
            tracksMediaPlayerInteractor.getStatePrepared(), tracksMediaPlayerInteractor.getStatePaused() -> {
                startPlayer()
            }
        }
    }

    fun onDestroy() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.release()
    }

    fun onSaveInstanceState() {
        spendTimeTextView.text = "00:00"
        playImageView.setImageResource(R.drawable.play_light_mode_button)
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.stop()
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
    }
}