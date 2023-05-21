package com.vaiwork.playlistmaker.ui.audioplayer.view_model

sealed interface SpendTimeState {
    object Default: SpendTimeState
    data class Changed(val timeString: String): SpendTimeState
}