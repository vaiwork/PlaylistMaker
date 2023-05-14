package com.vaiwork.playlistmaker.presentation.tracksmediaplayer

interface TracksMediaPlayerView {
    fun setSpendTime(text: String)

    fun setNightPauseImage()

    fun setLightPauseImage()

    fun setLightPlayImage()

    fun activatePlayImage(value: Boolean)
}