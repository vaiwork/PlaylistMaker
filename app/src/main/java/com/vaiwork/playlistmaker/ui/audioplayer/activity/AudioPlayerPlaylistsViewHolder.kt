package com.vaiwork.playlistmaker.ui.audioplayer.activity

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class AudioPlayerPlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistViewBottomSheetImageView: ImageView = itemView.findViewById(R.id.playlist_view_bottom_sheet_image_view)
    private val playlistViewBottomSheetTitleTextView: TextView = itemView.findViewById(R.id.playlist_view_bottom_sheet_title_text_view)
    private val playlistViewBottomSheetTracksCountTextView: TextView = itemView.findViewById(R.id.playlist_view_bottom_sheet_tracks_count_text_view)

    fun bind(playlist: Playlist, itemClickListener: AudioPlayerPlaylistsAdapter.PlaylistClickListener) {
        if (playlist.playlistCoverLocalUri == "") {
            Glide.with(playlistViewBottomSheetImageView)
                .load(R.drawable.playlist_placeholder_image_mini)
                .transform(
                    CenterInside(),
                    RoundedCorners(
                        playlistViewBottomSheetImageView.resources.getDimensionPixelSize(
                            R.dimen.activity_pleer_album_image_corner_radius
                        )
                    )
                ).into(playlistViewBottomSheetImageView)
            playlistViewBottomSheetImageView.setBackgroundColor(255)
        } else {
            Glide.with(playlistViewBottomSheetImageView)
                .load(playlist.playlistCoverLocalUri)
                .transform(
                    RoundedCorners(
                        playlistViewBottomSheetImageView.resources.getDimensionPixelSize(
                            R.dimen.activity_pleer_album_image_corner_radius
                        )
                    )
                ).into(playlistViewBottomSheetImageView)
        }
        playlistViewBottomSheetTitleTextView.text = playlist.playlistTitle
        when(playlist.playlistTracksNumber % 10) {
            1 -> playlistViewBottomSheetTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трек"
            2,3,4 -> playlistViewBottomSheetTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " трека"
            else -> playlistViewBottomSheetTracksCountTextView.text = playlist.playlistTracksNumber.toString() + " треков"
        }

        itemView.setOnClickListener {
            itemClickListener.onPlaylistClick(playlist)
        }
    }
}