package com.vaiwork.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT_CONTENT = "SEARCH_EDIT_TEXT_CONTENT"
    }

    private var editableText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<EditText>(R.id.activity_search_edit_text)
        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(SEARCH_EDIT_TEXT_CONTENT))
        }

        val clearImageView = findViewById<ImageView>(R.id.activity_search_clear_image_view)

        val editViewTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImageView.visibility = clearButtonVisibility(s)
                editableText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(editViewTextWatcher)

        clearImageView.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            searchEditText.setText("")
        }

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
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