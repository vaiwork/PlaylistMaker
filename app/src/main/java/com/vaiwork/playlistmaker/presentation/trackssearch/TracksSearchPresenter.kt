package com.vaiwork.playlistmaker.presentation.trackssearch

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator

class TracksSearchPresenter(
    private val tracksSearchView: TracksSearchView,
    private val context: Context
    ) {
    private val tracksInteractor = Creator.provideTracksInteractor(context)
    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(context)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
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
        if (!newSearchText.isNullOrEmpty()) {
            tracksInteractor.searchTracks(newSearchText, object: TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {
                    handler.post {
                        tracksSearchView.showProgressBar(false)
                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            //tracksSearchView.showTracksList(true)
                        }
                        if (errorMessage != null) {
                            tracksSearchView.showErrorPlaceholderMessage(true)
                            showMessage("Что-то не так", errorMessage)
                        } else if (tracks.isEmpty()) {
                            tracksSearchView.showTracksList(false)
                            tracksSearchView.showEmptyPlaceholderMessage(true)
                            tracksSearchView.showErrorPlaceholderMessage(false)
                            tracksSearchView.showYourSearchers(false)
                        } else {
                            tracksSearchView.updateAdapter(tracks)
                            tracksSearchView.showTracksList(true)
                            tracksSearchView.showEmptyPlaceholderMessage(false)
                            tracksSearchView.showErrorPlaceholderMessage(false)
                            tracksSearchView.showYourSearchers(false)
                        }
                    }
                }
            }
            )
        }
    }

    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onDebounce(track: Track) {
        sharedPreferenceInteractor.addTrack(track, App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun onSaveInstanceState(outState: Bundle) {
        if (editableText != null) {
            outState.putString(SEARCH_EDIT_TEXT_CONTENT, editableText)
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            tracksSearchView.setEditText(editableText)
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

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            if (additionalMessage.isNotEmpty()) {
                tracksSearchView.showToast(additionalMessage)
            }
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
}