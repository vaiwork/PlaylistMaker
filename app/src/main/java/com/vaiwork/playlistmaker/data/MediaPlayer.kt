package com.vaiwork.playlistmaker.data

import android.media.MediaPlayer

data class MediaPlayer(val mediaPlayer: MediaPlayer = MediaPlayer(),
                       var playerState: Int = STATE_DEFAULT) {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val AUDIO_PLEER_DELAY = 500L
    }
}