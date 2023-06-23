package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerInteractor
import com.vaiwork.playlistmaker.domain.api.TracksMediaPlayerRepository

class TracksMediaPlayerInteractorImpl(
    private val tracksMediaPlayerRepository: TracksMediaPlayerRepository
) : TracksMediaPlayerInteractor {

    override fun startPlayer() {
        tracksMediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        tracksMediaPlayerRepository.pausePlayer()
    }

    override fun setDataSource(url: String) {
        tracksMediaPlayerRepository.setDataSource(url)
    }

    override fun prepareAsync() {
        tracksMediaPlayerRepository.prepareAsync()
    }

    override fun onPreparedListener(listener: (Any) -> Unit) {
        tracksMediaPlayerRepository.onPreparedListener(listener)
    }

    override fun onCompletionListener(listener: (Any) -> Unit) {
        tracksMediaPlayerRepository.onCompletionListener(listener)
    }

    override fun release() {
        tracksMediaPlayerRepository.release()
    }

    override fun reset() {
        tracksMediaPlayerRepository.reset()
    }

    override fun stop() {
        tracksMediaPlayerRepository.stop()
    }

    override fun setPlayerState(state: Int) {
        tracksMediaPlayerRepository.setPlayerState(state)
    }

    override fun getPlayerState(): Int {
        return tracksMediaPlayerRepository.getPlayerState()
    }

    override fun getCurrentPosition(): Int {
        return tracksMediaPlayerRepository.getCurrentPosition()
    }

    override fun getStatePrepared(): Int {
        return tracksMediaPlayerRepository.getStatePrepared()
    }

    override fun getStatePlaying(): Int {
        return tracksMediaPlayerRepository.getStatePlaying()
    }

    override fun getStatePaused(): Int {
        return tracksMediaPlayerRepository.getStatePaused()
    }

    override fun getStateDefault(): Int {
        return tracksMediaPlayerRepository.getStateDefault()
    }

    override fun getAudioPleerDelay(): Long {
        return tracksMediaPlayerRepository.getAudioPleerDelay()
    }
}