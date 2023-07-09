package com.vaiwork.playlistmaker.data.media

import android.media.MediaPlayer
import com.vaiwork.playlistmaker.data.MediaPlayerClient

class TracksMediaPlayerClient(
    private val mediaPlayer: MediaPlayer = MediaPlayer(),
) : MediaPlayerClient {

    var playerState: Int = STATE_DEFAULT

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val AUDIO_PLAYER_DELAY = 500L
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun reset() {
        mediaPlayer.reset()
        playerState = STATE_DEFAULT
    }

    override fun stop() {
        mediaPlayer.stop()
        playerState = STATE_DEFAULT
    }

    override fun setCurrentPlayerState(state: Int) {
        playerState = state
    }

    override fun getCurrentPlayerState(): Int {
        return playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun onPreparedListener(listener: (Any) -> Unit) {
        mediaPlayer.setOnPreparedListener(listener)
    }

    override fun onCompletionListener(listener: (Any) -> Unit) {
        mediaPlayer.setOnCompletionListener(listener)
    }

    override fun getStatePrepared(): Int {
        return STATE_PREPARED
    }

    override fun getStatePlaying(): Int {
        return STATE_PLAYING
    }

    override fun getStatePaused(): Int {
        return STATE_PAUSED
    }

    override fun getStateDefault(): Int {
        return STATE_DEFAULT
    }

    override fun getAudioPleerDelay(): Long {
        return AUDIO_PLAYER_DELAY
    }
}