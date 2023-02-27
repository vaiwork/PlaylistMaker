package com.vaiwork.playlistmaker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.SharedPreferences


class SearchHistory (private var sharedPrefs: SharedPreferences) {

    fun addItemToSharedPrefs(itemTrack: Track) {
        var tracks = Gson().fromJson<ArrayList<Track>>(sharedPrefs.getString(App.HISTORY_TRACKS, null), object : TypeToken<List<Track>>() {}.type)
        if (tracks == null) {
            tracks = ArrayList<Track>()
            tracks.add(itemTrack)
        } else {
            var cloneTrackIndex: Int = -1
            for(track: Track in tracks) {
                if (track.trackId == itemTrack.trackId) {
                    cloneTrackIndex = tracks.indexOf(track)
                    break
                }
            }
            if (cloneTrackIndex != -1) {
                tracks.removeAt(cloneTrackIndex)
                tracks.add(0,itemTrack)
            } else {
                tracks.add(0,itemTrack)
                if (tracks.size > 10) {
                    tracks.removeAt(10)
                }
            }
        }
        sharedPrefs.edit().putString(App.HISTORY_TRACKS, Gson().toJson(tracks, object : TypeToken<List<Track>>() {}.type)).apply()
    }

    fun clearItemsFromSharedPrefs() {
        sharedPrefs.edit().putString(App.HISTORY_TRACKS, "").apply()
    }

    fun getItemsFromSharedPrefs(): ArrayList<Track>? {
        return Gson().fromJson<ArrayList<Track>>(sharedPrefs.getString(App.HISTORY_TRACKS, ""), object : TypeToken<List<Track>>() {}.type)
    }

}