package com.vaiwork.playlistmaker.ui.media.fragment

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistImageView: ImageView = itemView.findViewById(R.id.playlist_view_image_view)
    private val playlistTitleTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_title)
    private val playlistTracksCountTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_tracks_count)

    fun bind(playlist: Playlist) {
        if (playlist.playlistCoverLocalUri != "") {
            playlistImageView.setImageURI(Uri.parse(playlist.playlistCoverLocalUri))
        } else {
            playlistImageView.setImageResource(R.drawable.placeholder_album_image_light_mode)
            playlistImageView.setBackgroundColor(255)
        }
        playlistTitleTextView.text = playlist.playlistTitle
        when(playlist.playlistTracksNumber % 10) {
            1 -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трек"
            2,3,4 -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трекa"
            else -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " треков"
        }

        itemView.setOnClickListener {
            // TODO:
        }
    }
}