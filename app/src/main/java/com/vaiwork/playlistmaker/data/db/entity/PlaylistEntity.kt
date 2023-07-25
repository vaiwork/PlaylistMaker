package com.vaiwork.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val playlistTitle: String,
    val playlistDescription: String,
    val playlistCoverLocalUri: String,
    val playlistTracks: String,
    val playlistTracksNumber: Int,
    val addedDateTime: Long
)