package com.vaiwork.playlistmaker.domain.api

interface TracksMediaPlayerRepository {
    fun startPlayer()

    fun pausePlayer()

    fun setDataSource(url: String)

    fun prepareAsync()

    fun onPreparedListener(listener: (Any) -> Unit)

    fun onCompletionListener(listener: (Any) -> Unit)

    fun release()

    fun reset()

    fun stop()

    fun setPlayerState(state: Int)

    fun getPlayerState(): Int

    fun getCurrentPosition(): Int

    fun getStatePrepared(): Int

    fun getStatePlaying(): Int

    fun getStatePaused(): Int

    fun getStateDefault(): Int

    fun getAudioPleerDelay(): Long
}