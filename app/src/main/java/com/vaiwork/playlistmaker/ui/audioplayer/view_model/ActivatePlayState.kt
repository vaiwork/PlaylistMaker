package com.vaiwork.playlistmaker.ui.audioplayer.view_model

sealed interface ActivatePlayState {
    object Default : ActivatePlayState
    data class Changed(var isEnabled: Boolean) : ActivatePlayState
}