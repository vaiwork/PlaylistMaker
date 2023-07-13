package com.vaiwork.playlistmaker.ui.audioplayer.activity

sealed interface LikeButtonState {
    object Default: LikeButtonState
    object ClickedAdd: LikeButtonState
    object ClickedRemove: LikeButtonState
}