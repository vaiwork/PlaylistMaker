package com.vaiwork.playlistmaker.presentation.tracksmediaplayer

import com.vaiwork.playlistmaker.ui.audioplayer.AudioPlayerState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TracksMediaPlayerView : MvpView {

    /*
    fun setNightPauseImage()

    fun setLightPauseImage()

    fun setLightPlayImage()
    */

    // Методы, меняющие внешний вид экрана

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: AudioPlayerState)

    // Методы «одноразовых событий»

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSpendTime(text: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun activatePlayImage(value: Boolean)

}