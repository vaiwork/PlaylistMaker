package com.vaiwork.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track

class TrackAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var itemClickListener: TrackClickListener = TrackClickListener {  }

    private var buttonClearHistoryClickListener: ClearHistoryClickListener = ClearHistoryClickListener {  }

    private var tracks: ArrayList<Track> = ArrayList()

    private var isHistoryAdapter: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType === R.layout.track_view) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
            TrackViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.track_clear_history_button, parent, false)
            TrackClearHistoryButtonView(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.track_view -> (holder as TrackViewHolder).bind(tracks[position], itemClickListener)
            R.layout.track_clear_history_button -> (holder as TrackClearHistoryButtonView).bind(buttonClearHistoryClickListener)
        }
    }

    override fun getItemCount(): Int {
        return if (isHistoryAdapter) {
            tracks.size + 1
        } else {
            tracks.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHistoryAdapter) {
            when (position) {
                tracks.size -> R.layout.track_clear_history_button
                else -> R.layout.track_view
            }
        } else {
            R.layout.track_view
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface ClearHistoryClickListener {
        fun onClearHistoryClick()
    }

    fun setHistoryAdapter() {
        isHistoryAdapter = true
    }

    fun unsetHistoryAdapter() {
        isHistoryAdapter = false
    }

    fun setItemClickListener(clickListener: TrackClickListener) {
        itemClickListener = clickListener
    }

    fun setClearHistoryClickListener(clickListener: ClearHistoryClickListener) {
        buttonClearHistoryClickListener = clickListener
    }

    fun setTracks(_tracks: ArrayList<Track>) {
        tracks = _tracks
    }

    fun clearTracks() {
        tracks.clear()
    }

    fun addTracks(_tracks: ArrayList<Track>) {
        tracks.addAll(_tracks)
    }
}