package com.vaiwork.playlistmaker.domain.usecases

import android.media.MediaPlayer
import com.vaiwork.playlistmaker.data.TracksMediaPlayer

class TracksMediaPlayerInteractor {

    private val tracksMediaPlayer: TracksMediaPlayer = TracksMediaPlayer()

    fun startPlayer() {
        tracksMediaPlayer.startPlayer()
    }
    fun pausePlayer() {
        tracksMediaPlayer.pausePlayer()
    }

    fun setDataSource(url: String) {
        tracksMediaPlayer.setDataSource(url)
    }

    fun prepareAsync() {
        tracksMediaPlayer.prepareAsync()
    }

    fun OnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        tracksMediaPlayer.OnPreparedListener(listener)
    }

    fun OnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        tracksMediaPlayer.OnCompletionListener(listener)
    }

    fun release() {
        tracksMediaPlayer.release()
    }

    fun stop() {
        tracksMediaPlayer.stop()
    }

    fun setPlayerState(state: Int) {
        tracksMediaPlayer.setCurrentPlayerState(state)
    }

    fun getPlayerState(): Int {
        return tracksMediaPlayer.getCurrentPlayerState()
    }

    fun getCurrentPosition(): Int {
        return tracksMediaPlayer.getCurrentPosition()
    }

    fun getStatePrepared(): Int {
        return TracksMediaPlayer.STATE_PREPARED
    }

    fun getStatePlaying(): Int {
        return TracksMediaPlayer.STATE_PLAYING
    }

    fun getStatePaused(): Int {
        return TracksMediaPlayer.STATE_PAUSED
    }

    fun getStateDefault(): Int {
        return TracksMediaPlayer.STATE_DEFAULT
    }

    fun getAudioPleerDelay(): Long {
        return TracksMediaPlayer.AUDIO_PLEER_DELAY
    }
}