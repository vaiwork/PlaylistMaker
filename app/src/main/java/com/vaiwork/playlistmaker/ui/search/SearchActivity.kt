package com.vaiwork.playlistmaker.ui.search

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
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchPresenter
import com.vaiwork.playlistmaker.presentation.trackssearch.TracksSearchView
import com.vaiwork.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.vaiwork.playlistmaker.util.Creator
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SearchActivity : MvpActivity(), TracksSearchView {

    private var tracksAdapter : TrackAdapter = TrackAdapter()

    @InjectPresenter
    lateinit var tracksSearchPresenter: TracksSearchPresenter //? = null //Creator.provideTracksSearchPresenter(this, this)
    @ProvidePresenter
    fun providePresenter(): TracksSearchPresenter {
        return Creator.provideTracksSearchPresenter(
            context = this.applicationContext,
        )
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

    private lateinit var editViewTextWatcher: TextWatcher

    /*
    override fun onStart() {
        super.onStart()
        //tracksSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        //tracksSearchPresenter?.attachView(this)
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //tracksSearchPresenter = (this.applicationContext as? App)?.tracksSearchPresenter
        //if (tracksSearchPresenter == null) {
        //    tracksSearchPresenter = Creator.provideTracksSearchPresenter(this)
            //(this.applicationContext as? App)?.tracksSearchPresenter = tracksSearchPresenter
        //}
        //tracksSearchPresenter?.attachView(this)

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
            tracksSearchPresenter.clearHistoryTracks()
            tracksAdapter.setTracks(ArrayList())
            recyclerView.adapter = tracksAdapter
            showTracksList(false)
            showYourSearchers(false)

        }

        if (tracksSearchPresenter.getHistoryTracks().isNotEmpty()) {
            tracksAdapter.setHistoryAdapter()
            tracksAdapter.setTracks(tracksSearchPresenter.getHistoryTracks())
            recyclerView.adapter = tracksAdapter
            showTracksList(true)
            showYourSearchers(true)
        }

        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(TracksSearchPresenter.SEARCH_EDIT_TEXT_CONTENT))
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
                    if (tracksSearchPresenter.getHistoryTracks().isNotEmpty()) {
                        tracksAdapter.setTracks(tracksSearchPresenter.getHistoryTracks())
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
                    tracksSearchPresenter.searchDebounce(
                        changedText = s.toString() ?: ""
                    )
                }
                tracksSearchPresenter.setEditableText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editViewTextWatcher.let { searchEditText.addTextChangedListener(it) }
        searchEditText.addTextChangedListener(editViewTextWatcher)
        searchEditText.setOnClickListener{
            tracksSearchPresenter.searchRequest(searchEditText.text.toString())
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
            if (tracksSearchPresenter.clickDebounce()) {
                tracksSearchPresenter.onDebounce(it)

                val audioPlayerActivityIntent = Intent(this, AudioPlayerActivity::class.java)
                startActivity(audioPlayerActivityIntent)
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

    /*
    override fun onDestroy() {
        super.onDestroy()
        //tracksSearchPresenter?.detachView()
        editViewTextWatcher.let { searchEditText.removeTextChangedListener(it) }

        //if (isFinishing()) {
            // Очищаем ссылку на Presenter в Application
            //(this.application as? App)?.tracksSearchPresenter = null
        //}
    }
    */

    /*
    override fun onPause() {
        super.onPause()
        //tracksSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        //tracksSearchPresenter?.detachView()
    }
    */

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksSearchPresenter.onSaveInstanceState(outState)
        //tracksSearchPresenter?.detachView()
    }
     */

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText = findViewById(R.id.activity_search_edit_text)
        tracksSearchPresenter.onRestoreInstanceState(savedInstanceState)
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

    /*
    override fun showTracksList(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun updateTracksList(newTracksList: ArrayList<Track>) {
        tracksAdapter.clearTracks()
        tracksAdapter.addTracks(newTracksList)
    }

    override fun updateAdapter(newTracksList: ArrayList<Track>) {
        tracksAdapter.unsetHistoryAdapter()
        tracksAdapter.setTracks(newTracksList)
    }
    */

    fun showLoading() {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.VISIBLE
        searchErrorPlaceholderTextView.visibility = View.VISIBLE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE

        searchErrorPlaceholderTextView.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        recyclerView.visibility = View.GONE
        searchEmptyPlaceholderImageView.visibility = View.VISIBLE
        searchEmptyPlaceholderTextView.visibility = View.VISIBLE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE

        searchEmptyPlaceholderTextView.text = emptyMessage
    }

    fun showContent(tracks: ArrayList<Track>) {
        recyclerView.visibility = View.VISIBLE
        searchEmptyPlaceholderImageView.visibility = View.GONE
        searchEmptyPlaceholderTextView.visibility = View.GONE
        searchErrorPlaceholderImageView.visibility = View.GONE
        searchErrorPlaceholderTextView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
        progressBar.visibility = View.GONE

        tracksAdapter.unsetHistoryAdapter()
        tracksAdapter.clearTracks()
        tracksAdapter.addTracks(tracks)
        tracksAdapter.notifyDataSetChanged()
    }

    override fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    override fun setEditText(text: String?) {
        searchEditText.setText(text)
    }

    override fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Empty -> showEmpty(state.message)
        }
    }
}