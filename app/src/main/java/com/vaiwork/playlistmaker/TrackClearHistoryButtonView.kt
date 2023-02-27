package com.vaiwork.playlistmaker

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class TrackClearHistoryButtonView (itemView: View): RecyclerView.ViewHolder(itemView) {
    private val clearHistoryButton = itemView.findViewById<Button>(R.id.activity_search_clear_history_button)

    fun bind(buttonClearHistoryClickListener: OnClickedListener) {
        clearHistoryButton.setOnClickListener {
            buttonClearHistoryClickListener.OnClicked()
        }
    }
}