package com.vaiwork.playlistmaker.data

interface MediaPlayerClient {

    fun startPlayer()

    fun pausePlayer()

    fun setDataSource(url: String)

    fun prepareAsync()

    fun onPreparedListener(listener: (Any) -> Unit)

    fun onCompletionListener(listener: (Any) -> Unit)

    fun release()

    fun stop()

    fun setCurrentPlayerState(state: Int)

    fun getCurrentPlayerState(): Int

    fun getCurrentPosition(): Int

    fun getStatePrepared(): Int

    fun getStatePlaying(): Int

    fun getStatePaused(): Int

    fun getStateDefault(): Int

    fun getAudioPleerDelay(): Long
}