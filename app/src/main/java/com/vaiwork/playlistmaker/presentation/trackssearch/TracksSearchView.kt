package com.vaiwork.playlistmaker.presentation.trackssearch

import com.vaiwork.playlistmaker.domain.models.Track

interface TracksSearchView {

    fun showEmptyPlaceholderMessage(isVisible: Boolean)

    fun showErrorPlaceholderMessage(isVisible: Boolean)

    fun showTracksList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun showYourSearchers(isVisible: Boolean)

    fun setEditText(text: String?)

    fun updateTracksList(newTracksList: ArrayList<Track>)

    fun updateAdapter(newTracksList: ArrayList<Track>)

    fun showToast(message: String)
}