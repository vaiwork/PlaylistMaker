package com.vaiwork.playlistmaker.ui.audioplayer.activity

sealed interface AudioPlayerState {
    object PreparedPaused: AudioPlayerState

    data class Started(
        val isDarkTheme: Boolean
    ): AudioPlayerState
}