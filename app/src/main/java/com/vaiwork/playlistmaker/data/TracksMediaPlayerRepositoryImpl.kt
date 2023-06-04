package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerRepository

class TracksMediaPlayerRepositoryImpl(
    private val mediaPlayerClient: MediaPlayerClient
) : TracksMediaPlayerRepository {
    override fun startPlayer() {
        mediaPlayerClient.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerClient.pausePlayer()
    }

    override fun setDataSource(url: String) {
        mediaPlayerClient.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayerClient.prepareAsync()
    }

    override fun onPreparedListener(listener: (Any) -> Unit) {
        mediaPlayerClient.onPreparedListener(listener)
    }

    override fun onCompletionListener(listener: (Any) -> Unit) {
        mediaPlayerClient.onCompletionListener(listener)
    }

    override fun release() {
        mediaPlayerClient.release()
    }

    override fun reset() {
        mediaPlayerClient.reset()
    }

    override fun stop() {
        mediaPlayerClient.stop()
    }

    override fun setPlayerState(state: Int) {
        mediaPlayerClient.setCurrentPlayerState(state)
    }

    override fun getPlayerState(): Int {
        return mediaPlayerClient.getCurrentPlayerState()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerClient.getCurrentPosition()
    }

    override fun getStatePrepared(): Int {
        return mediaPlayerClient.getStatePrepared()
    }

    override fun getStatePlaying(): Int {
        return mediaPlayerClient.getStatePlaying()
    }

    override fun getStatePaused(): Int {
        return mediaPlayerClient.getStatePaused()
    }

    override fun getStateDefault(): Int {
        return mediaPlayerClient.getStateDefault()
    }

    override fun getAudioPleerDelay(): Long {
        return mediaPlayerClient.getAudioPleerDelay()
    }
}