package com.vaiwork.playlistmaker.ui.media.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class PlaylistsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaylistsViewHolder).bind(playlists[position])
    }

    fun setPlaylists(_playlists: List<Playlist>) {
        playlists = _playlists
    }

    fun clearTracks() {
        playlists = emptyList()
    }

    fun addTracks(_playlists: List<Playlist>) {
        playlists = playlists + _playlists
    }
}