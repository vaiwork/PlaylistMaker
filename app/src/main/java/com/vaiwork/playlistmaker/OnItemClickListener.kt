package com.vaiwork.playlistmaker

import com.vaiwork.playlistmaker.domain.entities.Track

interface OnItemClickedListener {
    fun OnItemClicked(track: Track)
}