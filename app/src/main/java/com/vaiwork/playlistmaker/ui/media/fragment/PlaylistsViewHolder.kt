package com.vaiwork.playlistmaker.ui.media.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistImageView: ImageView = itemView.findViewById(R.id.playlist_view_image_view)
    private val playlistTitleTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_title)
    private val playlistTracksCountTextView: TextView = itemView.findViewById(R.id.playlist_view_text_view_tracks_count)

    fun bind(playlist: Playlist, playlistClickListener: PlaylistsAdapter.PlaylistClickListener) {
        if (playlist.playlistCoverLocalUri != "") {
            Glide.with(playlistImageView)
                .load(playlist.playlistCoverLocalUri)
                .transform(
                    RoundedCorners(
                        playlistImageView.resources.getDimensionPixelSize(
                            R.dimen.activity_pleer_album_image_corner_radius
                        )
                    )
                ).into(playlistImageView)
        } else {
            Glide.with(playlistImageView)
                .load(R.drawable.playlist_placeholder_image_mini)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        playlistImageView.resources.getDimensionPixelSize(
                            R.dimen.activity_pleer_album_image_corner_radius
                        )
                    )
                ).into(playlistImageView)
            playlistImageView.setBackgroundColor(255)
        }
        playlistTitleTextView.text = playlist.playlistTitle
        when(playlist.playlistTracksNumber % 10) {
            1 -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трек"
            2,3,4 -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трекa"
            else -> playlistTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " треков"
        }

        itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlist)
        }
    }
}