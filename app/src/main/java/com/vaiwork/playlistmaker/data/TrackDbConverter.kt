package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.db.entity.TrackEntity
import com.vaiwork.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDbConverter {
    fun map(track: Track?): TrackEntity? {

        if (track != null) {
            return TrackEntity(
                track.trackId,
                track.artworkUrl100,
                track.trackName,
                track.artistName,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
                track.previewUrl,
                System.currentTimeMillis()
            )
        } else {
            return null
        }
    }

    fun map(track: TrackEntity?): Track? {
        if (track != null) {
            return Track(
                track.trackId,
                track.trackName,
                track.artistName,
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss:SS",
                    Locale.getDefault()
                ).parse("1970-01-01 00:" + track.trackTimeMillis + ":00")?.time ?: 0,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
        } else {
            return null
        }
    }
}