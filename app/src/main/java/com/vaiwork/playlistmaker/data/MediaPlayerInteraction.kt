package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.Track

interface MediaPlayerInteraction {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(clickedTrack: Track)
    fun playbackControl()
    fun spendTimeControl()
}