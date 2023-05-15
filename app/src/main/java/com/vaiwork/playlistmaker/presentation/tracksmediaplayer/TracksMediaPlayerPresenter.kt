package com.vaiwork.playlistmaker.presentation.tracksmediaplayer

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.presentation.settings.SettingsView
import com.vaiwork.playlistmaker.ui.audioplayer.AudioPlayerState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.Locale

class TracksMediaPlayerPresenter(
    private val context: Context
) : MvpPresenter<TracksMediaPlayerView>() {
    private val tracksMediaPlayerInteractor = Creator.provideTracksMediaPlayerInteractor()
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(context)

    private lateinit var lastClickedTrack: Track

    private val audioPleerHandler = Handler(Looper.getMainLooper())
    private val audioPleerRunnable = Runnable {
        spendTimeControl()
    }

    //private var view: TracksMediaPlayerView? = null

    /*
    fun attachView(view: TracksMediaPlayerView) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
    */
    private fun spendTimeControl() {
        if (tracksMediaPlayerInteractor.getPlayerState() == tracksMediaPlayerInteractor.getPlayerState()) {
            audioPleerHandler.postDelayed(audioPleerRunnable, tracksMediaPlayerInteractor.getAudioPleerDelay())
            viewState.setSpendTime(SimpleDateFormat("mm:ss", Locale.getDefault()).format(tracksMediaPlayerInteractor.getCurrentPosition()))
        }
    }

    private fun startPlayer() {
        tracksMediaPlayerInteractor.startPlayer()
        viewState.render(
            AudioPlayerState.Started(
                isDarkTheme = sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)
            )
        )
        /*
        if (sharedPreferenceInteractor.getBoolean(App.SETTINGS, MODE_PRIVATE, App.DARK_MODE, false)) {
            view?.setNightPauseImage()
        } else {
            view?.setLightPauseImage()
        }
        */
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePlaying())
        spendTimeControl()
    }

    private fun pausePlayer() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.pausePlayer()
        viewState.render(
            AudioPlayerState.PreparedPaused
        )
        //view?.setLightPlayImage()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePaused())
    }

    fun preparePlayer() {
        val tracks = getHistoryTracks()
        lastClickedTrack = tracks[0]
        tracksMediaPlayerInteractor.setDataSource(lastClickedTrack.previewUrl)
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.onPreparedListener {
            viewState.activatePlayImage(true)
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
        tracksMediaPlayerInteractor.onCompletionListener {
            viewState.render(
                AudioPlayerState.PreparedPaused
            )
            viewState.setSpendTime("00:00")
            //view?.setLightPlayImage()
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
    }

    fun setDefaultPlayerState() {
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStateDefault())
    }

    fun playbackControl() {
        when(tracksMediaPlayerInteractor.getPlayerState()) {
            tracksMediaPlayerInteractor.getStatePlaying() -> {
                pausePlayer()
            }
            tracksMediaPlayerInteractor.getStatePrepared(), tracksMediaPlayerInteractor.getStatePaused() -> {
                startPlayer()
            }
        }
    }

    override fun onDestroy() {
        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.release()
    }

    fun onSaveInstanceState() {
        viewState.render(
            AudioPlayerState.PreparedPaused
        )
        viewState.setSpendTime("00:00")
        //view?.setLightPlayImage()

        audioPleerHandler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.stop()
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
    }

    private fun getHistoryTracks(): ArrayList<Track> {
        return sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun setAlbumImage(placeholder: Int, resourceRequestOptions: Int, imageView: ImageView) {
        Glide.
        with(context).
        load(lastClickedTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).
        placeholder(placeholder).
        apply(RequestOptions.bitmapTransform(RoundedCorners(context.resources.getDimensionPixelSize(resourceRequestOptions)))).
        into(imageView)
    }

    fun getTrackName(): String {
        return lastClickedTrack.trackName
    }

    fun getTrackArtistName(): String {
        return lastClickedTrack.artistName
    }

    fun getTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(lastClickedTrack.trackTime)
    }

    fun getTrackCollection(): String {
        return lastClickedTrack.collectionName
    }

    fun getTrackYear(): CharSequence {
        return lastClickedTrack.releaseDate.subSequence(0,4)
    }

    fun getTrackPrimaryGenre(): String {
        return lastClickedTrack.primaryGenreName
    }

    fun getTrackCountry(): String {
        return lastClickedTrack.country
    }
}