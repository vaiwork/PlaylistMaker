package com.vaiwork.playlistmaker

import android.content.Context
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

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
        // Mock Track List
        /*val trackMockList: ArrayList<Track> = arrayListOf(
            Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
        )
         */
    }

    private var editableText: String? = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = findViewById<RecyclerView>(R.id.activity_search_search_recycler_view)
        var songsAdapter: TrackAdapter

        val searchEmptyPlaceholderImageView = findViewById<ImageView>(R.id.activity_search_empty_placeholder_image_view)
        val searchEmptyPlaceholderTextView = findViewById<TextView>(R.id.activity_search_empty_placeholder_text_view)

        val searchErrorPlaceholderImageView = findViewById<ImageView>(R.id.activity_search_error_placeholder_image_view)
        val searchErrorPlaceholderTextView = findViewById<TextView>(R.id.activity_search_error_placeholder_text_view)
        val searchUpdatePlaceholderButton = findViewById<Button>(R.id.activity_search_update_placeholder_button)

        val searchEditText = findViewById<EditText>(R.id.activity_search_edit_text)
        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT))
        }

        val clearImageView = findViewById<ImageView>(R.id.activity_search_clear_image_view)

        val editViewTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchEmptyPlaceholderImageView.visibility = View.GONE
                searchEmptyPlaceholderTextView.visibility = View.GONE
                searchErrorPlaceholderImageView.visibility = View.GONE
                searchErrorPlaceholderTextView.visibility = View.GONE
                searchUpdatePlaceholderButton.visibility = View.GONE
                clearImageView.visibility = clearButtonVisibility(s)
                if (s.isNullOrEmpty()) {
                    tracks = ArrayList<Track>()
                    songsAdapter = TrackAdapter(tracks)
                    recyclerView.visibility = View.GONE
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
                                songsAdapter = TrackAdapter(tracks)
                                recyclerView.visibility = View.VISIBLE
                                searchErrorPlaceholderImageView.visibility = View.GONE
                                searchErrorPlaceholderTextView.visibility = View.GONE
                                searchUpdatePlaceholderButton.visibility = View.GONE
                                searchEmptyPlaceholderImageView.visibility = View.GONE
                                searchEmptyPlaceholderTextView.visibility = View.GONE
                                recyclerView.adapter = songsAdapter
                            } else {
                                recyclerView.visibility = View.GONE
                                searchEmptyPlaceholderImageView.visibility = View.VISIBLE
                                searchEmptyPlaceholderTextView.visibility = View.VISIBLE
                                searchErrorPlaceholderImageView.visibility = View.GONE
                                searchErrorPlaceholderTextView.visibility = View.GONE
                                searchUpdatePlaceholderButton.visibility = View.GONE
                            }
                        } else {
                            searchEmptyPlaceholderImageView.visibility = View.GONE
                            searchEmptyPlaceholderTextView.visibility = View.GONE
                            searchErrorPlaceholderImageView.visibility = View.VISIBLE
                            searchErrorPlaceholderTextView.visibility = View.VISIBLE
                            searchUpdatePlaceholderButton.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        t.printStackTrace()
                        searchEmptyPlaceholderImageView.visibility = View.GONE
                        searchEmptyPlaceholderTextView.visibility = View.GONE
                        searchErrorPlaceholderImageView.visibility = View.VISIBLE
                        searchErrorPlaceholderTextView.visibility = View.VISIBLE
                        searchUpdatePlaceholderButton.visibility = View.VISIBLE
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

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
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

        val searchEditText = findViewById<EditText>(R.id.activity_search_edit_text)
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
}