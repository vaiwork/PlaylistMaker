package com.vaiwork.playlistmaker.ui.audioplayer.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
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
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerState
import com.vaiwork.playlistmaker.ui.audioplayer.activity.LikeButtonState
import com.vaiwork.playlistmaker.util.App
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TracksMediaPlayerViewModel(
    private val tracksMediaPlayerInteractor: TracksMediaPlayerInteractor,
    private val sharedPreferenceInteractor: SharedPreferenceInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    application: Application
) : AndroidViewModel(application) {

    private lateinit var lastClickedTrack: Track

    private var timerJob: Job? = null

    private val favouriteTrack = MutableLiveData<Track>()
    fun observeFavouriteTrack(): LiveData<Track> = favouriteTrack

    private val audioPlayerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeAudioPlayerState(): LiveData<AudioPlayerState> = audioPlayerStateLiveData

    private val likeButtonStateLiveData = MutableLiveData<LikeButtonState>(LikeButtonState.Default)
    fun observeLikeButtonState(): LiveData<LikeButtonState> = likeButtonStateLiveData

    private val setSpendTimeLiveData = MutableLiveData<SpendTimeState>(SpendTimeState.Default)
    fun observeSetSpendTime(): LiveData<SpendTimeState> = setSpendTimeLiveData

    private val activatePlayImageLiveData =
        MutableLiveData<ActivatePlayState>(ActivatePlayState.Default)

    fun observeActivatePlayImage(): LiveData<ActivatePlayState> = activatePlayImageLiveData

    private val isTrackFromFavourites = MutableLiveData<Boolean>()
    fun observeIsTrackFromFavourites(): LiveData<Boolean> = isTrackFromFavourites

    fun changeTrack(track: Track) {
        lastClickedTrack = track
        preparePlayer()
    }

    private fun startPlayer() {
        tracksMediaPlayerInteractor.startPlayer()
        startTimer()
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
            do {
                delay(300L)
                setSpendTime(
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                        tracksMediaPlayerInteractor.getCurrentPosition()
                    )
                )
            }
            while (tracksMediaPlayerInteractor.getPlayerState() == tracksMediaPlayerInteractor.getStatePlaying())
        }
    }

    private fun preparePlayer() {
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
            timerJob?.cancel()
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
    }

    fun preparePlayerTrack(trackId: Int) {
        viewModelScope.launch {
            if (favouriteTracksInteractor.getFavouriteTracks().first()?.find { track -> track.trackId == trackId } == null) {
                val tracks = getHistoryTracks()
                favouriteTrack.postValue(tracks[0])
            } else {
                favouriteTrack.postValue(
                    favouriteTracksInteractor.getFavouriteTracks().first()!!.find { track -> track.trackId == trackId }!!)
            }
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
        pausePlayer()
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

    fun likeButtonClicked() {
        likeButtonStateLiveData.value = LikeButtonState.Default
    }

    fun likeControl() {
        when (likeButtonStateLiveData.value) {
            is LikeButtonState.Default -> {
                if (isTrackFromFavourites.value!!) {
                    renderState(LikeButtonState.ClickedRemove)
                    deleteTrackFromFavourites()
                } else {
                    renderState(LikeButtonState.ClickedAdd)
                    addTrackToFavourites()
                }
            }
            else -> {
                renderState(LikeButtonState.Default)
            }
        }
    }

    fun isTrackFromFavouritesControl(trackId: Int) {
        viewModelScope.launch {
            isTrackFromFavourites.postValue(
                favouriteTracksInteractor.selectTrackByTrackId(trackId).first() != null
            )
        }
    }

    private fun renderState(state: LikeButtonState) {
        likeButtonStateLiveData.postValue(state)
    }

    private fun addTrackToFavourites() {
        viewModelScope.launch {
            favouriteTracksInteractor.addTrackToFavourite(lastClickedTrack)
        }
    }

    private fun deleteTrackFromFavourites() {
        viewModelScope.launch {
            favouriteTracksInteractor.deleteTrackFromFavourite(lastClickedTrack)
        }
    }
}