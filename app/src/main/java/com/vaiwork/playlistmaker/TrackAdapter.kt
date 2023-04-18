package com.vaiwork.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.domain.entities.Track


class TrackAdapter (
    private val tracks: ArrayList<Track>,
    private val itemClickListener: OnItemClickedListener,
    private val buttonClearHistoryClickListener: OnClickedListener,
    private val isHistoryAdapter: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType === R.layout.track_view) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
            TrackViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.track_clear_history_button, parent, false)
            TrackClearHistoryButtonView(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.track_view -> (holder as TrackViewHolder).bind(tracks[position], itemClickListener)
            R.layout.track_clear_history_button -> (holder as TrackClearHistoryButtonView).bind(buttonClearHistoryClickListener)
        }
    }

    override fun getItemCount(): Int {
        return if (isHistoryAdapter) {
            tracks.size + 1
        } else {
            tracks.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHistoryAdapter) {
            when (position) {
                tracks.size -> R.layout.track_clear_history_button
                else -> R.layout.track_view
            }
        } else {
            R.layout.track_view
        }
    }

}