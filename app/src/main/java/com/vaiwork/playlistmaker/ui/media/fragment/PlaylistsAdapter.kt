package com.vaiwork.playlistmaker.ui.media.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class PlaylistsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var playlistClickListener: PlaylistClickListener = PlaylistClickListener { }

    private var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaylistsViewHolder).bind(playlists[position], playlistClickListener)
    }

    fun setPlaylists(_playlists: List<Playlist>) {
        playlists = _playlists
    }

    fun clearPlaylists() {
        playlists = emptyList()
    }

    fun addPlaylists(_playlists: List<Playlist>) {
        playlists = playlists + _playlists
    }

    fun setItemClickListener(clickListener: PlaylistsAdapter.PlaylistClickListener) {
        playlistClickListener = clickListener
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}