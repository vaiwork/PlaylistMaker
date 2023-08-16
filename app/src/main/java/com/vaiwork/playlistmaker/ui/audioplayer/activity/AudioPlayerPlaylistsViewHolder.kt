package com.vaiwork.playlistmaker.ui.audioplayer.activity

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Playlist

class AudioPlayerPlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistViewBottomSheetImageView: ImageView = itemView.findViewById(R.id.playlist_view_bottom_sheet_image_view)
    private val playlistViewBottomSheetTitleTextView: TextView = itemView.findViewById(R.id.playlist_view_bottom_sheet_title_text_view)
    private val playlistViewBottomSheetTracksCountTextView: TextView = itemView.findViewById(R.id.playlist_view_bottom_sheet_tracks_count_text_view)

    fun bind(playlist: Playlist, itemClickListener: AudioPlayerPlaylistsAdapter.PlaylistClickListener) {
        if (playlist.playlistCoverLocalUri == "") {
            playlistViewBottomSheetImageView.setImageResource(R.drawable.playlist_placeholder_image_mini)
            playlistViewBottomSheetImageView.setBackgroundColor(255)
            playlistViewBottomSheetImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            playlistViewBottomSheetImageView.setImageURI(Uri.parse(playlist.playlistCoverLocalUri))
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