package com.vaiwork.playlistmaker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceRepository
import com.vaiwork.playlistmaker.domain.models.Track

class SharedPreferenceRepositoryImpl(
    private val dbClient: DBClient
): SharedPreferenceRepository {

    override fun addTrack(dto: Track, sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String) {
        var tracks = getTracks(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
        if (tracks.isEmpty()) {
            tracks = ArrayList()
            tracks.add(dto)
        } else {
            var cloneTrackIndex: Int = -1
            for(_track: Track in tracks) {
                if (_track.trackId == dto.trackId) {
                    cloneTrackIndex = tracks.indexOf(_track)
                    break
                }
            }
            if (cloneTrackIndex != -1) {
                tracks.removeAt(cloneTrackIndex)
                tracks.add(0,dto)
            } else {
                tracks.add(0,dto)
                if (tracks.size > 10) {
                    tracks.removeAt(10)
                }
            }
        }
        dbClient.putString(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, Gson().toJson(tracks, object : TypeToken<List<Track>>() {}.type))
    }

    override fun getTracks(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String): ArrayList<Track> {
        val dtoTracks = dbClient.getTracks(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
        val tracks = ArrayList<Track>()
        if (dtoTracks != null) {
            for (_track in dtoTracks) {
                tracks.add(
                    Track(
                        _track.trackId,
                        _track.trackName,
                        _track.artistName,
                        _track.trackTime,
                        _track.artworkUrl100,
                        _track.collectionName,
                        _track.releaseDate,
                        _track.primaryGenreName,
                        _track.country,
                        _track.previewUrl
                    )
                )
            }
        }
        return tracks
    }

    override fun clear(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String) {
        dbClient.putString(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, "")
    }

    override fun getBoolean(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean {
        return dbClient.getBooleanKey(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, defaultValue)
    }

    override fun switchTheme(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        value: Boolean
    ) {
        dbClient.setBooleanKey(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, value)
    }
}