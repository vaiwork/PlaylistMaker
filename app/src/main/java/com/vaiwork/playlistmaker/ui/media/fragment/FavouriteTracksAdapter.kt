package com.vaiwork.playlistmaker.ui.media.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track
import com.vaiwork.playlistmaker.ui.media.fragment.FavouriteTracksAdapter.TrackClickListener

class FavouriteTracksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: TrackClickListener = TrackClickListener { }

    private var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TrackViewHolder).bind(tracks[position], itemClickListener)
    }

    fun setTracks(_tracks: List<Track>) {
        tracks = _tracks
    }

    fun setItemClickListener(clickListener: TrackClickListener) {
        itemClickListener = clickListener
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}