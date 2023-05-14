package com.vaiwork.playlistmaker.presentation.tracksmediaplayer

import com.vaiwork.playlistmaker.domain.models.Track

interface TracksMediaPlayerView {
    fun setSpendTime(text: String)

    fun setNightPauseImage()

    fun setLightPauseImage()

    fun setLightPlayImage()

    fun activatePlayImage(value: Boolean)
}