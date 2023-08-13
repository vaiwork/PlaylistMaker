package com.vaiwork.playlistmaker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity
import com.vaiwork.playlistmaker.data.db.entity.PlaylistsTrackEntity
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class DbConverter {

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

    fun map(playlistEntity: PlaylistEntity?): Playlist? {
        return if (playlistEntity != null) {
            Playlist(
                playlistEntity.playlistTitle,
                playlistEntity.playlistDescription,
                playlistEntity.playlistCoverLocalUri,
                Gson().fromJson(
                    playlistEntity.playlistTracks,
                    object : TypeToken<List<Int>>() {}.type
                ),
                playlistEntity.playlistTracksNumber
            )
        } else {
            null
        }
    }

    fun map(playlist: Playlist?, playlistId: Int = -1, addedDateTime: Long = -1): PlaylistEntity? {
        return if (playlist != null) {
            PlaylistEntity(
                if (playlistId == -1) {0} else {playlistId},
                playlist.playlistTitle,
                playlist.playlistDescription,
                playlist.playlistCoverLocalUri,
                if (!playlist.playlistTracks.isNullOrEmpty()) {
                    Gson().toJson(playlist.playlistTracks, object : TypeToken<List<Int>>() {}.type)
                } else { "[]" },
                if (!playlist.playlistTracks.isNullOrEmpty()) { playlist.playlistTracks.size} else {0},
                if (addedDateTime == (-1).toLong()) { System.currentTimeMillis() } else { addedDateTime }
            )
        } else {
            null
        }
    }

    fun pmap(track: Track?): PlaylistsTrackEntity? {

        if (track != null) {
            return PlaylistsTrackEntity(
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

    fun pmap(track: PlaylistsTrackEntity?): Track? {
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