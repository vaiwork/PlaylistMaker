package com.vaiwork.playlistmaker.presentation.trackssearch

import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.TracksState

interface TracksSearchView {

    fun setEditText(text: String?)

    /*
    fun showEmptyPlaceholderMessage(isVisible: Boolean)

    fun showErrorPlaceholderMessage(isVisible: Boolean)

    fun showTracksList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun showYourSearchers(isVisible: Boolean)



    fun updateTracksList(newTracksList: ArrayList<Track>)

    fun updateAdapter(newTracksList: ArrayList<Track>)

    //fun showToast(message: String)
*/
    //=============

    // Методы, меняющие внешний вид экрана

    fun render(state: TracksState)

    // Состояние «загрузки»
    fun showLoading()

    // Состояние «ошибки»
    fun showError(errorMessage: String)

    // Состояние «пустого списка»
    fun showEmpty(emptyMessage: String)

    // Состояние «контента»
    fun showContent(tracks: ArrayList<Track>)

    // Методы «одноразовых событий»

    fun showToast(additionalMessage: String)
}