package com.vaiwork.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private var playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun isPlaylistsEmptyCheck() {
        viewModelScope.launch {
            var playlists = playlistsInteractor.getPlaylists().first()
            if (!playlists.isNullOrEmpty()) {
                stateLiveData.postValue(PlaylistsState.ContentPlaylists(playlists))
            } else {
                stateLiveData.postValue(PlaylistsState.ErrorYouDoNotCreateAnyPlaylists)
            }
        }
    }
}