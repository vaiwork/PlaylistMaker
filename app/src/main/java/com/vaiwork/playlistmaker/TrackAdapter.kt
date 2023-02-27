package com.vaiwork.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackAdapter (
    private val tracks: ArrayList<Track>,
    private val itemClickListener: OnItemClickedListener
) : RecyclerView.Adapter<TrackViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], itemClickListener)

        holder.itemView.setOnClickListener{
            SearchHistory((holder.itemView.context.applicationContext as App).sharedPrefs).addItemToSharedPrefs(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}