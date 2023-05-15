package com.vaiwork.playlistmaker.ui.search.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.activity.TracksState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class TracksSearchViewModel(
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

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(getApplication<Application>())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private var lastSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var tracks = ArrayList<Track>()

    private var editableText: String? = ""
    private var isClickAllowed = true

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            tracksInteractor.searchTracks(newSearchText, object: TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {
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
            }
            )
        }
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onDebounce(track: Track) {
        sharedPreferenceInteractor.addTrack(track, App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(searchRunnable)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            setEditText(editableText)
        }
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
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