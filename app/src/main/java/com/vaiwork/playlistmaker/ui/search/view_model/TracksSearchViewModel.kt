package com.vaiwork.playlistmaker.ui.search.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.fragment.TracksState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val sharedPreferenceInteractor: SharedPreferenceInteractor,
    application: Application
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val showToastState = MutableLiveData<String>()
    fun observeShowToastState(): LiveData<String> = showToastState

    private val setEditTextState = MutableLiveData<String?>()
    fun observeSetEditTextState(): LiveData<String?> = setEditTextState

    private val toastState = MutableLiveData<ToastState>(ToastState.None)
    fun observeToastState(): LiveData<ToastState> = toastState

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
    }

    private var lastSearchText: String? = null
    private var tracks = ArrayList<Track>()

    private var editableText: String? = ""

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect { pair ->
                    processResults(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResults(foundTracks: ArrayList<Track>?, errorMessage: String?) {
        if (foundTracks != null) {
            tracks.clear()
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                renderState(
                    TracksState.Error(
                        errorMessage = "Что-то не так",
                    )
                )
                showToast(errorMessage)
            }

            tracks.isEmpty() -> {
                renderState(
                    TracksState.Empty(
                        message = "Ничего не найдено",
                    )
                )
            }

            else -> {
                renderState(
                    TracksState.Content(
                        tracks = tracks,
                    )
                )
            }
        }
    }

    private val movieSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        searchRequest(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText != changedText) {
            this.lastSearchText = changedText
            movieSearchDebounce(changedText)
        }
    }

    fun onDebounce(track: Track) {
        sharedPreferenceInteractor.addTrack(track, App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            setEditText(editableText)
        }
    }

    fun setEditableText(text: String) {
        editableText = text
    }

    fun getHistoryTracks(): ArrayList<Track> {
        return sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun clearHistoryTracks() {
        sharedPreferenceInteractor.clear(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    private fun showToast(additionalMessage: String) {
        showToastState.postValue(additionalMessage)
    }

    private fun setEditText(text: String?) {
        setEditTextState.postValue(text)
    }

    fun toastWasShown() {
        toastState.value = ToastState.None
    }
}