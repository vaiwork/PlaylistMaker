package com.vaiwork.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.databinding.FragmentSearchBinding
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.search.view_model.ToastState
import com.vaiwork.playlistmaker.ui.search.view_model.TracksSearchViewModel
import com.vaiwork.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val tracksSearchViewModel: TracksSearchViewModel by viewModel()

    private var tracksAdapter: TrackAdapter = TrackAdapter()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 10000L
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEmptyPlaceholderImageView: ImageView
    private lateinit var searchEmptyPlaceholderTextView: TextView
    private lateinit var searchErrorPlaceholderImageView: ImageView
    private lateinit var searchErrorPlaceholderTextView: TextView
    private lateinit var searchUpdatePlaceholderButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var clearImageView: ImageView
    private lateinit var yourSearcherTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            tracksSearchViewModel.onDebounce(track)
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerActivity)
        }

        yourSearcherTextView = binding.activitySearchTextViewYourSearches
        recyclerView = binding.activitySearchSearchRecyclerView
        searchEmptyPlaceholderImageView = binding.activitySearchEmptyPlaceholderImageView
        searchEmptyPlaceholderTextView = binding.activitySearchEmptyPlaceholderTextView
        searchErrorPlaceholderImageView = binding.activitySearchErrorPlaceholderImageView
        searchErrorPlaceholderTextView = binding.activitySearchErrorPlaceholderTextView
        searchUpdatePlaceholderButton = binding.activitySearchUpdatePlaceholderButton
        searchEditText = binding.activitySearchEditText
        clearImageView = binding.activitySearchClearImageView
        progressBar = binding.activitySearchProgressBar

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
                    tracksSearchViewModel.searchDebounce(
                        changedText = s.toString()
                    )
                    showProgressBar(true)
                }
                tracksSearchViewModel.setEditableText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editViewTextWatcher.let { searchEditText.addTextChangedListener(it) }
        searchEditText.addTextChangedListener(editViewTextWatcher)
        searchEditText.setOnClickListener {
            tracksSearchViewModel.searchRequest(searchEditText.text.toString())
        }

        clearImageView.setOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
            searchEditText.setText("")
        }

        searchUpdatePlaceholderButton.setOnClickListener {
            searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        tracksAdapter.setItemClickListener {
            onTrackClickDebounce(it)
        }

        tracksSearchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        tracksSearchViewModel.observeShowToastState().observe(viewLifecycleOwner) {
            showToast(it)
        }

        tracksSearchViewModel.observeSetEditTextState().observe(viewLifecycleOwner) {
            setEditText(it)
        }

        tracksSearchViewModel.observeToastState().observe(viewLifecycleOwner) { toastState ->
            if (toastState is ToastState.Show) {
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
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        searchEditText = binding.activitySearchEditText
        if (savedInstanceState != null) {
            tracksSearchViewModel.onRestoreInstanceState(savedInstanceState)
        }
    }
}