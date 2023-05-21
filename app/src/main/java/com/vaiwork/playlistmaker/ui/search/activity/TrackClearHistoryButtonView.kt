package com.vaiwork.playlistmaker.ui.search.activity

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.vaiwork.playlistmaker.R
import com.vaiwork.playlistmaker.ui.search.activity.TrackAdapter

class TrackClearHistoryButtonView (itemView: View): RecyclerView.ViewHolder(itemView) {
    private val clearHistoryButton = itemView.findViewById<Button>(R.id.activity_search_clear_history_button)

    fun bind(buttonClearHistoryClickListener: TrackAdapter.ClearHistoryClickListener) {
        clearHistoryButton.setOnClickListener {
            buttonClearHistoryClickListener.onClearHistoryClick()
        }
    }
}