package com.vaiwork.playlistmaker.ui.media.fragment

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistImageView: ImageView = itemView.findViewById(R.id.playlist_view_image_view)
    private val playlistTitleTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_title)
    private val playlistTracksCountTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_tracks_count)

    fun bind(playlist: Playlist) {
        playlistImageView.setImageURI(Uri.parse(playlist.playlistCoverLocalUri))
        playlistTitleTextView.text = playlist.playlistTitle
        playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " Tracks"

        itemView.setOnClickListener {
            //
        }
    }
}