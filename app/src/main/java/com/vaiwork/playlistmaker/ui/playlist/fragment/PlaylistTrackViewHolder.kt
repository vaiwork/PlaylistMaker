package com.vaiwork.playlistmaker.ui.playlist.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val songImage: ImageView = itemView.findViewById(R.id.song_view_song_image_view)
    private val songName: TextView = itemView.findViewById(R.id.song_view_song_name_text_view)
    private val songExecutorName: TextView =
        itemView.findViewById(R.id.song_view_song_executor_name_text_view)
    private val songArrowForward: ImageView = itemView.findViewById(R.id.song_view_arrow_forward_image_view)
    private val songTime: TextView = itemView.findViewById(R.id.song_view_song_time_text_view)
    fun bind(songTrack: Track, itemClickListener: PlaylistTrackAdapter.PlaylistTrackClickListener, itemLongClickListener: PlaylistTrackAdapter.PlaylistTrackLongClickListener) {
        Glide.with(itemView)
            .load(songTrack.artworkUrl100)
            .placeholder(R.drawable.ic_round_music_video_24)
            .into(songImage)
        songName.text = songTrack.trackName
        songExecutorName.text = songTrack.artistName
        songTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(songTrack.trackTimeMillis)

        itemView.setOnClickListener {
            itemClickListener.onPlaylistTrackClick(songTrack)
        }

        itemView.setOnLongClickListener {
            itemLongClickListener.onPlaylistTrackLongClick(songTrack)
        }
    }
}