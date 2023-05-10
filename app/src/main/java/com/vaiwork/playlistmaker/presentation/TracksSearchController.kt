package com.vaiwork.playlistmaker.presentation

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.App
import com.vaiwork.playlistmaker.Creator
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.TrackAdapter

class TracksSearchController(
    private val activity: Activity,
    private val adapter: TrackAdapter
    ) {
    private val tracksInteractor = Creator.provideTracksInteractor(activity)

    private val sharedPreferenceInteractor = Creator.provideSharedPreferenceInteractor(activity)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEmptyPlaceholderImageView: ImageView
    private lateinit var searchEmptyPlaceholderTextView: TextView
    private lateinit var searchErrorPlaceholderImageView: ImageView
    private lateinit var searchErrorPlaceholderTextView: TextView
    private lateinit var searchUpdatePlaceholderButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var clearImageView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var yourSearcherTextView: TextView
    private lateinit var progressBar: ProgressBar

    private var tracks = ArrayList<Track>()

    private var editableText: String? = ""

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchRequest() }

    fun onCreate(savedInstanceState: Bundle?) {
        yourSearcherTextView = activity.findViewById(R.id.activity_search_text_view_your_searches)
        recyclerView = activity.findViewById(R.id.activity_search_search_recycler_view)
        searchEmptyPlaceholderImageView = activity.findViewById(R.id.activity_search_empty_placeholder_image_view)
        searchEmptyPlaceholderTextView = activity.findViewById(R.id.activity_search_empty_placeholder_text_view)
        searchErrorPlaceholderImageView = activity.findViewById(R.id.activity_search_error_placeholder_image_view)
        searchErrorPlaceholderTextView = activity.findViewById(R.id.activity_search_error_placeholder_text_view)
        searchUpdatePlaceholderButton = activity.findViewById(R.id.activity_search_update_placeholder_button)
        searchEditText = activity.findViewById(R.id.activity_search_edit_text)
        clearImageView = activity.findViewById(R.id.activity_search_clear_image_view)
        progressBar = activity.findViewById(R.id.activity_search_progress_bar)
        toolbar = activity.findViewById(R.id.search_toolbar)

        adapter.setClearHistoryClickListener {
            sharedPreferenceInteractor.clear(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
            adapter.setTracks(ArrayList())
            recyclerView.adapter = adapter
            recyclerView.visibility = View.GONE
            yourSearcherTextView.visibility = View.GONE
        }

        tracks = sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
        if (tracks.isNotEmpty()) {
            adapter.setHistoryAdapter()
            adapter.setTracks(tracks)
            recyclerView.adapter = adapter
            recyclerView.visibility = View.VISIBLE
            yourSearcherTextView.visibility = View.VISIBLE
        }

        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT))
        }

        val editViewTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                recyclerView.visibility = View.GONE
                yourSearcherTextView.visibility = View.GONE
                searchEmptyPlaceholderImageView.visibility = View.GONE
                searchEmptyPlaceholderTextView.visibility = View.GONE
                searchErrorPlaceholderImageView.visibility = View.GONE
                searchErrorPlaceholderTextView.visibility = View.GONE
                searchUpdatePlaceholderButton.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchEmptyPlaceholderImageView.visibility = View.GONE
                searchEmptyPlaceholderTextView.visibility = View.GONE
                searchErrorPlaceholderImageView.visibility = View.GONE
                searchErrorPlaceholderTextView.visibility = View.GONE
                searchUpdatePlaceholderButton.visibility = View.GONE
                clearImageView.visibility = clearButtonVisibility(s)
                if (s.isNullOrEmpty()) {
                    tracks = sharedPreferenceInteractor.getTracks(App.SETTINGS, MODE_PRIVATE, App.HISTORY_TRACKS)
                    if (tracks.isNotEmpty()) {
                        yourSearcherTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                        adapter.setHistoryAdapter()
                    } else {
                        tracks = ArrayList()
                        yourSearcherTextView.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }
                    adapter.setTracks(tracks)
                    recyclerView.adapter = adapter
                } else {
                    progressBar.visibility = View.VISIBLE
                    searchDebounce()
                }
                editableText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(editViewTextWatcher)
        searchEditText.setOnClickListener{
            searchRequest()
        }

        clearImageView.setOnClickListener {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            searchEditText.setText("")
        }

        toolbar.setNavigationOnClickListener { activity.onBackPressed() }

        searchUpdatePlaceholderButton.setOnClickListener{
            searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

    }

    private fun searchRequest() {
        if (!searchEditText.text.isNullOrEmpty()) {
            tracksInteractor.searchTracks(searchEditText.text.toString(), object: TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {
                    handler.post {
                        progressBar.visibility = View.GONE
                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            recyclerView.visibility = View.VISIBLE
                        }
                        if (errorMessage != null) {
                            showMessage("Что-то не так", errorMessage)
                        } else if (tracks.isEmpty()) {
                            recyclerView.visibility = View.GONE
                            searchEmptyPlaceholderImageView.visibility = View.VISIBLE
                            searchEmptyPlaceholderTextView.visibility = View.VISIBLE
                            searchErrorPlaceholderImageView.visibility = View.GONE
                            searchErrorPlaceholderTextView.visibility = View.GONE
                            searchUpdatePlaceholderButton.visibility = View.GONE
                            yourSearcherTextView.visibility = View.GONE
                        } else {
                            adapter.unsetHistoryAdapter()
                            adapter.setTracks(tracks)
                            recyclerView.visibility = View.VISIBLE
                            searchErrorPlaceholderImageView.visibility = View.GONE
                            searchErrorPlaceholderTextView.visibility = View.GONE
                            searchUpdatePlaceholderButton.visibility = View.GONE
                            searchEmptyPlaceholderImageView.visibility = View.GONE
                            searchEmptyPlaceholderTextView.visibility = View.GONE
                            yourSearcherTextView.visibility = View.GONE
                        }
                    }
                }
            }
            )
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
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
        searchEditText = activity.findViewById(R.id.activity_search_edit_text)
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            searchEditText.setText(editableText)
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
                Toast.makeText(activity.applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}