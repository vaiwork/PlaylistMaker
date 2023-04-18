package com.vaiwork.playlistmaker.presentation.presenter.AudioPleer

import android.app.Activity
import android.os.Handler
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.vaiwork.playlistmaker.domain.usecases.TracksMediaPlayerInteractor

class AudioPleerPresenter(
    private var view: AudioPleerView?,
    dataSource: String,
    private val tracksMediaPlayerInteractor: TracksMediaPlayerInteractor,
    private val handler: Handler
    ) {

    private val audioPleerRunnable = Runnable {
        spendTimeControl()
    }

    init {
        tracksMediaPlayerInteractor.setDataSource(dataSource)
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.OnPreparedListener {
            enablePlayButton()
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
        tracksMediaPlayerInteractor.OnCompletionListener{
            setDefaultSpendTimeTextView()
            setPlayButton()
            tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
        }
    }

    fun spendTimeControl() {
        if (tracksMediaPlayerInteractor.getStatePlaying() == tracksMediaPlayerInteractor.getPlayerState()) {
            handler.postDelayed(audioPleerRunnable,  tracksMediaPlayerInteractor.getAudioPleerDelay())
            setCurrentSpendTime()
        }
    }


    fun setPlayButton() {
        view?.setPlayButton()
    }

    fun setDefaultSpendTimeTextView() {
        view?.setDefaultSpendTime()
    }

    fun enablePlayButton() {
        view?.setImageIsEnabled()
    }

    fun pausePlayer() {
        handler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.pausePlayer()
        setPlayButton()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePaused())
    }

    fun startPlayer() {
        tracksMediaPlayerInteractor.startPlayer()
        setDarkOrLightPauseButton()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePlaying())
        spendTimeControl()
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

    fun clickedPlayButton() {
        playbackControl()
    }

    fun destroyActivity() {
        handler.removeCallbacks(audioPleerRunnable)
        tracksMediaPlayerInteractor.release()
    }

    fun saveInstantState() {
        handler.removeCallbacks(audioPleerRunnable)
        view?.setPlayButton()
        view?.setDefaultSpendTime()
        tracksMediaPlayerInteractor.stop()
        tracksMediaPlayerInteractor.prepareAsync()
        tracksMediaPlayerInteractor.setPlayerState(tracksMediaPlayerInteractor.getStatePrepared())
    }

    fun setImageWithPlaceholder(activity: Activity, imageView: ImageView, url: String, placeholder: Int, requestOptions: RequestOptions) {
        Glide.with(activity).load(url).placeholder(placeholder).apply(requestOptions).into(imageView)
    }

    fun setCurrentSpendTime() {
        view?.setCurrentSpendTime()
    }

    fun setDarkOrLightPauseButton() {
        view?.setDarkOrLightPauseButton()
    }

    fun getCurrentPosition(): Int {
        return tracksMediaPlayerInteractor.getCurrentPosition()
    }
}