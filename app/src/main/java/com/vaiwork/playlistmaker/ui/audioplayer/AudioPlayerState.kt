package com.vaiwork.playlistmaker.ui.audioplayer

sealed interface AudioPlayerState {
    object Prepared: AudioPlayerState
    object Playing: AudioPlayerState
}