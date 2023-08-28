package com.vaiwork.playlistmaker.ui.playlist.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track

class PlaylistTrackAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: PlaylistTrackClickListener = PlaylistTrackClickListener { }

    private var itemLongClickListener: PlaylistTrackLongClickListener = PlaylistTrackLongClickListener {
        true
    }

    private var tracks: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaylistTrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaylistTrackViewHolder).bind(tracks[position], itemClickListener, itemLongClickListener)
    }

    fun setTracks(tracksList: List<Track>) {
        tracks = tracksList
    }

    fun setItemClickListener(clickListener: PlaylistTrackClickListener) {
        itemClickListener = clickListener
    }

    fun setItemLongClickListener(clickListener: PlaylistTrackLongClickListener) {
        itemLongClickListener = clickListener
    }

    fun interface PlaylistTrackClickListener {
        fun onPlaylistTrackClick(track: Track)
    }

    fun interface PlaylistTrackLongClickListener {
        fun onPlaylistTrackLongClick(track: Track): Boolean
    }
}