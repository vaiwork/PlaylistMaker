package com.vaiwork.playlistmaker.presentation.trackssearch

import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.TracksState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TracksSearchView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
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

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: TracksState)

    // Методы «одноразовых событий»

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)
}