package com.vaiwork.playlistmaker.presentation.trackssearch

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.TracksState
import com.vaiwork.playlistmaker.util.App
import com.vaiwork.playlistmaker.util.Creator
import moxy.MvpPresenter

class TracksSearchPresenter(
    private val context: Context
    ) : MvpPresenter<TracksSearchView>() {
    //private var tracksSearchView: TracksSearchView? = null
    //private var tracksState: TracksState? = null

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
            //
            //tracksSearchView.showLoading()

            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(newSearchText, object: TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {
                    handler.post {
                        //tracksSearchView.showProgressBar(false)
                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            //tracksSearchView.showTracksList(true)
                        }
                        //
                        when {
                            errorMessage != null -> {
                                renderState(
                                    TracksState.Error(
                                        errorMessage = "Что-то не так",
                                    )
                                )
                                viewState.showToast(errorMessage)
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

                        /*
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
                        }*/
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

    /*
    fun onSaveInstanceState(outState: Bundle) {
        if (editableText != null) {
            outState.putString(SEARCH_EDIT_TEXT_CONTENT, editableText)
        }
    }
    */

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(searchRunnable)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            viewState.setEditText(editableText)
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

    /*
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            if (additionalMessage.isNotEmpty()) {
                tracksSearchView.showToast(additionalMessage)
            }
        }
    }
    */

    fun setEditableText(text: String) {
        editableText = text
    }

    fun getHistoryTracks(): ArrayList<Track> {
        return sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    fun clearHistoryTracks() {
        sharedPreferenceInteractor.clear(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
    }

    /*
    fun attachView(view: TracksSearchView) {
        this.tracksSearchView = view
        tracksState?.let { view.render(it) }
    }

    fun detachView() {
        this.tracksSearchView = null
    }
    */

    private fun renderState(state: TracksState) {
        //this.tracksState = state
        //this.tracksSearchView?.render(state)
        viewState.render(state)
    }
}