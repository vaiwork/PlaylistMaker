package com.vaiwork.playlistmaker.ui.search.fragment

import com.vaiwork.playlistmaker.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: ArrayList<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState
}