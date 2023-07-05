package com.vaiwork.playlistmaker.ui.audioplayer.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerState
import com.vaiwork.playlistmaker.util.App
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TracksMediaPlayerViewModel(
    private val tracksMediaPlayerInteractor: TracksMediaPlayerInteractor,
    private val sharedPreferenceInteractor: SharedPreferenceInteractor,
    application: Application
) : AndroidViewModel(application) {

    private lateinit var lastClickedTrack: Track

    private var timerJob: Job? = null

    private val audioPlayerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeAudioPlayerState(): LiveData<AudioPlayerState> = audioPlayerStateLiveData

    private val setSpendTimeLiveData = MutableLiveData<SpendTimeState>(SpendTimeState.Default)
    fun observeSetSpendTime(): LiveData<SpendTimeState> = setSpendTimeLiveData

    private val activatePlayImageLiveData =
        MutableLiveData<ActivatePlayState>(ActivatePlayState.Default)

    fun observeActivatePlayImage(): LiveData<ActivatePlayState> = activatePlayImageLiveData

    private fun startPlayer() {
        tracksMediaPlayerInteractor.startPlayer()
        renderState(
            AudioPlayerState.Started(
                isDarkTheme = sharedPreferenceInteractor.getBoolean(
                    App.SETTINGS,
                    MODE_PRIVATE,
                    App.DARK_MODE,
                    false
                )
            )
        )
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePlaying())
        startTimer()
    }

    private fun pausePlayer() {
        timerJob?.cancel()
        tracksMediaPlayerInteractor.pausePlayer()
        renderState(
            AudioPlayerState.PreparedPaused
        )
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePaused())
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (tracksMediaPlayerInteractor.getPlayerState() == tracksMediaPlayerInteractor.getStatePlaying()) {
                delay(300L)
                setSpendTime(
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                        tracksMediaPlayerInteractor.getCurrentPosition()
                    )
                )
            }
            setSpendTime("00:00")
        }
    }

    fun preparePlayer() {
        val tracks = getHistoryTracks()
        lastClickedTrack = tracks[0]
        tracksMediaPlayerInteractor.reset()
        tracksMediaPlayerInteractor.setDataSource(lastClickedTrack.previewUrl)
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.onPreparedListener {
            setSpendTime("00:00")
            activatePlayImage(true)
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
        tracksMediaPlayerInteractor.onCompletionListener {
            renderState(
                AudioPlayerState.PreparedPaused
            )
            setSpendTime("00:00")
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
    }

    fun setDefaultPlayerState() {
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStateDefault())
    }

    fun playbackControl() {
        when (tracksMediaPlayerInteractor.getPlayerState()) {
            tracksMediaPlayerInteractor.getStatePlaying() -> {
                pausePlayer()
            }

            tracksMediaPlayerInteractor.getStatePrepared(), tracksMediaPlayerInteractor.getStatePaused() -> {
                startPlayer()
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        setSpendTime("00:00")
        tracksMediaPlayerInteractor.stop()
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
    }

    fun onSaveInstanceState() {
        timerJob?.cancel()
        renderState(
            AudioPlayerState.PreparedPaused
        )
        setSpendTime("00:00")
        tracksMediaPlayerInteractor.stop()
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
    }

    private fun getHistoryTracks(): ArrayList<Track> {
        return sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun setAlbumImage(placeholder: Int, resourceRequestOptions: Int, imageView: ImageView) {
        Glide.with(getApplication<Application>())
            .load(lastClickedTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(placeholder).apply(
            RequestOptions.bitmapTransform(
                RoundedCorners(
                    getApplication<Application>().resources.getDimensionPixelSize(
                        resourceRequestOptions
                    )
                )
            )
        ).into(imageView)
    }

    fun getTrackName(): String {
        return lastClickedTrack.trackName
    }

    fun getTrackArtistName(): String {
        return lastClickedTrack.artistName
    }

    fun getTrackTime(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(lastClickedTrack.trackTimeMillis)
    }

    fun getTrackCollection(): String {
        return lastClickedTrack.collectionName
    }

    fun getTrackYear(): CharSequence {
        return lastClickedTrack.releaseDate.subSequence(0, 4)
    }

    fun getTrackPrimaryGenre(): String {
        return lastClickedTrack.primaryGenreName
    }

    fun getTrackCountry(): String {
        return lastClickedTrack.country
    }

    private fun renderState(state: AudioPlayerState) {
        audioPlayerStateLiveData.postValue(state)
    }

    private fun setSpendTime(timeString: String) {
        setSpendTimeLiveData.postValue(SpendTimeState.Changed(timeString))
    }

    private fun activatePlayImage(isEnabled: Boolean) {
        activatePlayImageLiveData.postValue(ActivatePlayState.Changed(isEnabled))
    }

    fun spendTimeWasChanged() {
        setSpendTimeLiveData.value = SpendTimeState.Default
    }

    fun activatePlayImageChanged() {
        activatePlayImageLiveData.value = ActivatePlayState.Default
    }
}