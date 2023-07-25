package com.vaiwork.playlistmaker.ui.newplaylist.view_model

sealed interface NewPlaylistTitleState {
    object Default: NewPlaylistTitleState
    data class Changed(var str: String): NewPlaylistTitleState
}