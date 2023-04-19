package com.vaiwork.playlistmaker.data

import android.media.MediaPlayer

class TracksMediaPlayer(
    val mediaPlayer: MediaPlayer = MediaPlayer(),
    var playerState: Int = STATE_DEFAULT
) {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val AUDIO_PLAYER_DELAY = 500L
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    fun release() {
        mediaPlayer.release()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun setCurrentPlayerState(state: Int) {
        playerState = state
    }

    fun getCurrentPlayerState(): Int {
        return playerState
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun onPreparedListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    fun onCompletionListener(listener: (MediaPlayer) -> Unit) {
        mediaPlayer.setOnCompletionListener(listener)
    }
}
