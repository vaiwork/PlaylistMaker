package com.vaiwork.playlistmaker.ui.newplaylist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var coverLocalUri: String = ""

    private var playlist: Playlist? = null

    private val playlistChangedLiveData = MutableLiveData<Int>()
    fun observePlaylistChangedLiveData(): LiveData<Int> = playlistChangedLiveData

    private val playlistAddedLiveData = MutableLiveData<Int>()
    fun observePlaylistAddedLiveData(): LiveData<Int> = playlistAddedLiveData

    private val newPlaylistTitleMutableLiveData = MutableLiveData<NewPlaylistTitleState>()
    fun observeNewPlaylistTitle(): LiveData<NewPlaylistTitleState> = newPlaylistTitleMutableLiveData

    fun newPlaylistTitleController(str: String) {
        if (str.isNotEmpty()) {
            newPlaylistTitleMutableLiveData.postValue(NewPlaylistTitleState.Changed(str))
        } else {
            newPlaylistTitleMutableLiveData.postValue(NewPlaylistTitleState.Default)
        }
    }

    fun setPlaylistCoverLocalUri(coverLocalUriString: String) {
        this.coverLocalUri = coverLocalUriString
    }

    fun addPlaylist(title: String, description: String, update: Boolean = false) {
        if (!update) {
            viewModelScope.launch {
                val resultCode = playlistsInteractor.addPlaylist(
                    Playlist(
                        title,
                        description,
                        coverLocalUri,
                        emptyList(),
                        0
                    )
                ).first()
                playlistAddedLiveData.postValue(resultCode)
            }
        } else {
            viewModelScope.launch {
                if ( playlist?.playlistTitle != title
                    || playlist?.playlistDescription != description
                    || playlist?.playlistCoverLocalUri != coverLocalUri ) {
                    val resultCode = playlistsInteractor.updatePlaylist(
                        playlist!!, title, description, coverLocalUri
                    ).first()
                    playlistChangedLiveData.postValue(resultCode)
                }
            }
        }
    }

    fun mapStringToPlaylist(playlistString: String): Playlist? {
        playlist = playlistsInteractor.mapStringToPlaylist(playlistString)
        return playlist
    }

    fun isUpdateView(): Boolean {
        return playlist != null
    }
}