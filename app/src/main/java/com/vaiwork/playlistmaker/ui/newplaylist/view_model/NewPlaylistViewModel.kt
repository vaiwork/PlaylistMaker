package com.vaiwork.playlistmaker.ui.newplaylist.view_model

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import com.vaiwork.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var coverLocalUri: String = ""

    private val newPlaylistTitleMutableLiveData = MutableLiveData<NewPlaylistTitleState>()
    fun observeNewPlaylistTitle(): LiveData<NewPlaylistTitleState> = newPlaylistTitleMutableLiveData

    fun newPlaylistTitleController(str: String) {
        if (!str.isNullOrEmpty()) {
            newPlaylistTitleMutableLiveData.postValue(NewPlaylistTitleState.Changed(str))
        } else {
            newPlaylistTitleMutableLiveData.postValue(NewPlaylistTitleState.Default)
        }
    }

    fun setPlaylistCoverLocalUri(coverLocalUriString: String) {
        this.coverLocalUri = coverLocalUriString
    }

    fun addPlaylist(title: String, description: String) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    title,
                    description,
                    coverLocalUri,
                    emptyList(),
                    0
                )
            )
        }
    }
}