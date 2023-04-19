package com.vaiwork.playlistmaker.presentation.ui.AudioPleer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.entities.Track
import com.vaiwork.playlistmaker.domain.usecases.SharedPreferencesAPIInteractor
import com.vaiwork.playlistmaker.domain.usecases.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.presentation.ui.app.App
import com.vaiwork.playlistmaker.presentation.presenter.AudioPleer.AudioPleerPresenter
import com.vaiwork.playlistmaker.presentation.presenter.AudioPleer.AudioPleerView

class AudioPleerActivity : AppCompatActivity(), AudioPleerView {

    private val audioPleerHandler = Handler(Looper.getMainLooper())
    private lateinit var tracksMediaPlayerInteractor: TracksMediaPlayerInteractor
    private lateinit var clickedTrack: Track
    private lateinit var audioPleerPresenter: AudioPleerPresenter

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

        tracksMediaPlayerInteractor = TracksMediaPlayerInteractor()
        clickedTrack = SharedPreferencesAPIInteractor(applicationContext as App).getTracksSharedPref(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)[0]
        audioPleerPresenter = AudioPleerPresenter(this, clickedTrack.previewUrl, tracksMediaPlayerInteractor, audioPleerHandler)

        toolbar = findViewById(R.id.activity_pleer_back_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        playImageView = findViewById(R.id.activity_pleer_play_image_view)

        albumImageView = findViewById(R.id.activity_pleer_album_image)
        trackName = findViewById(R.id.activity_pleer_track_name)
        artistName = findViewById(R.id.activity_pleer_artist_name)
        timeTextView = findViewById(R.id.activity_pleer_time)
        albumTextView = findViewById(R.id.activity_pleer_album)
        yearTextView = findViewById(R.id.activity_pleer_year)
        styleTextView = findViewById(R.id.activity_pleer_style)
        countryTextView = findViewById(R.id.activity_pleer_country)
        fillTrackInformation()

        spendTimeTextView = findViewById(R.id.activity_pleer_spend_time_text_view)

        playImageView.setOnClickListener {
            audioPleerPresenter.clickedPlayButton()
        }
    }

    private fun fillTrackInformation() {
        audioPleerPresenter.setImageWithPlaceholder(this, albumImageView, clickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"), R.drawable.placeholder_album_image_light_mode, RequestOptions.bitmapTransform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.activity_pleer_album_image_corner_radius))))
        trackName.text = clickedTrack.trackName
        artistName.text = clickedTrack.artistName
        timeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(clickedTrack.trackTime)
        albumTextView.text = clickedTrack.collectionName
        yearTextView.text = clickedTrack.releaseDate.subSequence(0,4)
        styleTextView.text = clickedTrack.primaryGenreName
        countryTextView.text = clickedTrack.country
    }

    override fun onDestroy() {
        super.onDestroy()


        audioPleerPresenter.destroyActivity()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        audioPleerPresenter.saveInstantState()
    }

    override fun setDefaultSpendTime() {
        spendTimeTextView.text = resources.getString(R.string.activity_pleer_hint_spend_time)
    }

    override fun setCurrentSpendTime() {
        spendTimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(audioPleerPresenter.getCurrentPosition())
    }

    override fun setDarkOrLightPauseButton() {
        if (SharedPreferencesAPIInteractor(applicationContext as App).getBooleanKeySharedPref(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)) {
            playImageView.setImageResource(R.drawable.pause_night_mode_button)
        } else {
            playImageView.setImageResource(R.drawable.pause_light_mode_button)
        }
    }

    override fun setImageIsEnabled() {
        playImageView.isEnabled = true
    }

    override fun setPlayButton() {
        playImageView.setImageResource(R.drawable.play_light_mode_button)
    }
}