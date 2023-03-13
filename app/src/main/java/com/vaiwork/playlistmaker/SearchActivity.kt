package com.vaiwork.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), OnItemClickedListener, OnClickedListener {

    companion object {
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
    }

    private var editableText: String? = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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

    private lateinit var songsAdapter: TrackAdapter
    private var tracks = ArrayList<Track>()
    private var thisSearchActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        yourSearcherTextView = findViewById(R.id.activity_search_text_view_your_searches)
        recyclerView = findViewById(R.id.activity_search_search_recycler_view)
        if (SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs() != null) {
            tracks = SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs()!!
            songsAdapter = TrackAdapter(tracks, this,this, true)
            recyclerView.adapter = songsAdapter
            recyclerView.visibility = View.VISIBLE
            yourSearcherTextView.visibility = View.VISIBLE
        }

        searchEmptyPlaceholderImageView = findViewById(R.id.activity_search_empty_placeholder_image_view)
        searchEmptyPlaceholderTextView = findViewById(R.id.activity_search_empty_placeholder_text_view)

        searchErrorPlaceholderImageView = findViewById(R.id.activity_search_error_placeholder_image_view)
        searchErrorPlaceholderTextView = findViewById(R.id.activity_search_error_placeholder_text_view)
        searchUpdatePlaceholderButton = findViewById(R.id.activity_search_update_placeholder_button)

        searchEditText = findViewById(R.id.activity_search_edit_text)
        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT))
        }

        clearImageView = findViewById(R.id.activity_search_clear_image_view)

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
                    if (SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs() != null) {
                        tracks = SearchHistory((applicationContext as App).sharedPrefs).getItemsFromSharedPrefs()!!
                        yourSearcherTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                        songsAdapter = TrackAdapter(tracks, thisSearchActivity, thisSearchActivity, true)
                    } else {
                        tracks = ArrayList()
                        yourSearcherTextView.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        songsAdapter = TrackAdapter(tracks, thisSearchActivity, thisSearchActivity)
                    }
                    recyclerView.adapter = songsAdapter
                }
                editableText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(editViewTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val varITunesSearchApi = retrofit.create<iTunesSearchApi>()
                varITunesSearchApi.getTracks(editableText!!).enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.isSuccessful) {
                            val tracksJson = response.body()
                            if (tracksJson?.resultCount!! > 0) {
                                tracks = tracksJson.results
                                songsAdapter = TrackAdapter(tracks, thisSearchActivity, thisSearchActivity)
                                recyclerView.adapter = songsAdapter
                                recyclerView.visibility = View.VISIBLE
                                searchErrorPlaceholderImageView.visibility = View.GONE
                                searchErrorPlaceholderTextView.visibility = View.GONE
                                searchUpdatePlaceholderButton.visibility = View.GONE
                                searchEmptyPlaceholderImageView.visibility = View.GONE
                                searchEmptyPlaceholderTextView.visibility = View.GONE
                                yourSearcherTextView.visibility = View.GONE

                            } else {
                                recyclerView.visibility = View.GONE
                                searchEmptyPlaceholderImageView.visibility = View.VISIBLE
                                searchEmptyPlaceholderTextView.visibility = View.VISIBLE
                                searchErrorPlaceholderImageView.visibility = View.GONE
                                searchErrorPlaceholderTextView.visibility = View.GONE
                                searchUpdatePlaceholderButton.visibility = View.GONE
                                yourSearcherTextView.visibility = View.GONE
                            }
                        } else {
                            searchEmptyPlaceholderImageView.visibility = View.GONE
                            searchEmptyPlaceholderTextView.visibility = View.GONE
                            searchErrorPlaceholderImageView.visibility = View.VISIBLE
                            searchErrorPlaceholderTextView.visibility = View.VISIBLE
                            searchUpdatePlaceholderButton.visibility = View.VISIBLE
                            yourSearcherTextView.visibility = View.GONE
                            recyclerView.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        t.printStackTrace()
                        searchEmptyPlaceholderImageView.visibility = View.GONE
                        searchEmptyPlaceholderTextView.visibility = View.GONE
                        searchErrorPlaceholderImageView.visibility = View.VISIBLE
                        searchErrorPlaceholderTextView.visibility = View.VISIBLE
                        searchUpdatePlaceholderButton.visibility = View.VISIBLE
                        yourSearcherTextView.visibility = View.GONE
                    }

                })
            }
            false
        }

        clearImageView.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            searchEditText.setText("")
        }

        toolbar = findViewById(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        searchUpdatePlaceholderButton.setOnClickListener{
            searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (editableText != null) {
            outState.putString(SEARCH_EDIT_TEXT_CONTENT, editableText)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchEditText = findViewById(R.id.activity_search_edit_text)
        editableText = savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT)
        if (editableText != null) {
            searchEditText.setText(editableText)
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun OnItemClicked(track: Track) {
        SearchHistory((applicationContext as App).sharedPrefs).addItemToSharedPrefs(track)

        val audioPleerActivityIntent = Intent(this, AudioPleerActivity::class.java)
        startActivity(audioPleerActivityIntent)
    }

    override fun OnClicked() {
        SearchHistory((applicationContext as App).sharedPrefs).clearItemsFromSharedPrefs()
        tracks = ArrayList()
        songsAdapter = TrackAdapter(tracks, this, this)
        recyclerView.adapter = songsAdapter
        recyclerView.visibility = View.GONE
        yourSearcherTextView.visibility = View.GONE
    }
}