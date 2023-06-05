package com.vaiwork.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavouritesTracksViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData<FavouritesTracksState>()
    fun observeState(): LiveData<FavouritesTracksState> = stateLiveData

    init {
        stateLiveData.postValue(FavouritesTracksState.Error("Ваша медиатека пуста"))
    }

}