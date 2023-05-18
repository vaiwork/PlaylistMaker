package com.vaiwork.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.vaiwork.playlistmaker.ui.search.view_model.ToastState
import com.vaiwork.playlistmaker.ui.search.view_model.TracksSearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var tracksSearchViewModel: TracksSearchViewModel

    private var tracksAdapter : TrackAdapter = TrackAdapter()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tracksSearchViewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]

        yourSearcherTextView = findViewById(R.id.activity_search_text_view_your_searches)
        recyclerView = findViewById(R.id.activity_search_search_recycler_view)
        searchEmptyPlaceholderImageView = findViewById(R.id.activity_search_empty_placeholder_image_view)
        searchEmptyPlaceholderTextView = findViewById(R.id.activity_search_empty_placeholder_text_view)
        searchErrorPlaceholderImageView = findViewById(R.id.activity_search_error_placeholder_image_view)
        searchErrorPlaceholderTextView = findViewById(R.id.activity_search_error_placeholder_text_view)
        searchUpdatePlaceholderButton = findViewById(R.id.activity_search_update_placeholder_button)
        searchEditText = findViewById(R.id.activity_search_edit_text)
        clearImageView = findViewById(R.id.activity_search_clear_image_view)
        progressBar = findViewById(R.id.activity_search_progress_bar)
        toolbar = findViewById(R.id.search_toolbar)

        //
        tracksAdapter.setClearHistoryClickListener {
            tracksSearchViewModel.clearHistoryTracks()
            tracksAdapter.setTracks(ArrayList())
            recyclerView.adapter = tracksAdapter
            showTracksList(false)
            showYourSearchers(false)

        }

        if (tracksSearchViewModel.getHistoryTracks().isNotEmpty()) {
            tracksAdapter.setHistoryAdapter()
            tracksAdapter.setTracks(tracksSearchViewModel.getHistoryTracks())
            recyclerView.adapter = tracksAdapter
            showTracksList(true)
            showYourSearchers(true)
        }

        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(TracksSearchViewModel.SEARCH_EDIT_TEXT_CONTENT))
        }

        val editViewTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showTracksList(false)
                showYourSearchers(false)
                showEmptyPlaceholderMessage(false)
                showErrorPlaceholderMessage(false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showEmptyPlaceholderMessage(false)
                showErrorPlaceholderMessage(false)
                clearImageView.visibility = clearButtonVisibility(s)
                if (s.isNullOrEmpty()) {
                    if (tracksSearchViewModel.getHistoryTracks().isNotEmpty()) {
                        tracksAdapter.setTracks(tracksSearchViewModel.getHistoryTracks())
                        showYourSearchers(true)
                        showTracksList(true)
                        tracksAdapter.setHistoryAdapter()
                    } else {
                        tracksAdapter.setTracks(ArrayList())
                        showYourSearchers(false)
                        showTracksList(false)
                    }
                    recyclerView.adapter = tracksAdapter
                } else {
                    showProgressBar(true)
                    tracksSearchViewModel.searchDebounce(
                        changedText = s.toString() ?: ""
                    )
                }
                tracksSearchViewModel.setEditableText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editViewTextWatcher.let { searchEditText.addTextChangedListener(it) }
        searchEditText.addTextChangedListener(editViewTextWatcher)
        searchEditText.setOnClickListener{
            tracksSearchViewModel.searchRequest(searchEditText.text.toString())
        }

        clearImageView.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            searchEditText.setText("")
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }

        searchUpdatePlaceholderButton.setOnClickListener{
            searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        //

        tracksAdapter.setItemClickListener {
            if (tracksSearchViewModel.clickDebounce()) {
                tracksSearchViewModel.onDebounce(it)

                val audioPlayerActivityIntent = Intent(this, AudioPlayerActivity::class.java)
                startActivity(audioPlayerActivityIntent)
            }
        }

        tracksSearchViewModel.observeState().observe(this) {
            render(it)
        }

        tracksSearchViewModel.observeShowToastState().observe(this) {
            showToast(it)
        }

        tracksSearchViewModel.observeSetEditTextState().observe(this) {
            setEditText(it)
        }

        tracksSearchViewModel.observeToastState().observe(this) {toastState ->
            if(toastState is ToastState.Show) {
                showToast(toastState.additionalMessage)
                tracksSearchViewModel.toastWasShown()
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText = findViewById(R.id.activity_search_edit_text)
        tracksSearchViewModel.onRestoreInstanceState(savedInstanceState)
    }

    fun showTracksList(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showYourSearchers(isVisible: Boolean) {
        yourSearcherTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showEmptyPlaceholderMessage(isVisible: Boolean) {
        searchEmptyPlaceholderImageView.visibility = if (isVisible) View.VISIBLE else View.GONE
        searchEmptyPlaceholderTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showErrorPlaceholderMessage(isVisible: Boolean) {
        searchErrorPlaceholderImageView.visibility = if (isVisible) View.VISIBLE else View.GONE
        searchErrorPlaceholderTextView.visibility = if (isVisible) View.VISIBLE else View.GONE
        searchUpdatePlaceholderButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.VISIBLE
        searchErrorPlaceholderTextView.visibility = View.VISIBLE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE

        searchErrorPlaceholderTextView.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.VISIBLE
        searchEmptyPlaceholderTextView.visibility = View.VISIBLE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE

        searchEmptyPlaceholderTextView.text = emptyMessage
    }

    private fun showContent(tracks: ArrayList<Track>) {
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        tracksAdapter.unsetHistoryAdapter()
        tracksAdapter.clearTracks()
        tracksAdapter.addTracks(tracks)
        tracksAdapter.notifyDataSetChanged()
        recyclerView.adapter = tracksAdapter
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun setEditText(text: String?) {
        searchEditText.setText(text)
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Empty -> showEmpty(state.message)
        }
    }
}