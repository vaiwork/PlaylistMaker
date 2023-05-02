package com.vaiwork.playlistmaker

import com.vaiwork.playlistmaker.domain.models.Track

interface OnItemClickedListener {
    fun OnItemClicked(track: Track)
}