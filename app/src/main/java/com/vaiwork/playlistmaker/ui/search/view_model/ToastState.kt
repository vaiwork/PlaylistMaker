package com.vaiwork.playlistmaker.ui.search.view_model

sealed interface ToastState {
    object None : ToastState
    data class Show(val additionalMessage: String) : ToastState
}
