package com.vaiwork.playlistmaker.ui.media.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.util.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavouritesTracksViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavouritesTracksState>()
    fun observeState(): LiveData<FavouritesTracksState> = stateLiveData

    fun favouriteTracksMediaControl() {
        viewModelScope.launch {
            val tracks: List<Track> = favouriteTracksInteractor.getFavouriteTracks().first()
            if (tracks.isEmpty()) {
                stateLiveData.postValue(FavouritesTracksState.ErrorYourMediaEmpty)
            } else {
                stateLiveData.postValue(FavouritesTracksState.MediaNotEmpty(tracks))
            }
        }
    }

}