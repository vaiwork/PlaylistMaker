package com.vaiwork.playlistmaker.ui.audioplayer.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.ui.audioplayer.activity.AudioPlayerPlaylistsAdapter.PlaylistClickListener

class AudioPlayerPlaylistsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: PlaylistClickListener = PlaylistClickListener { }

    private var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return AudioPlayerPlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_view_bottom_sheet, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AudioPlayerPlaylistsViewHolder).bind(playlists[position], itemClickListener)
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

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }

    fun setItemClickListener(clickListener: PlaylistClickListener) {
        itemClickListener = clickListener
    }
}