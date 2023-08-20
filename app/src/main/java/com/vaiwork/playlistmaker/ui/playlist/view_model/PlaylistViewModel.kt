package com.vaiwork.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaiwork.playlistmaker.domain.db.PlaylistsInteractor
import com.vaiwork.playlistmaker.domain.db.PlaylistsTrackInteractor
import com.vaiwork.playlistmaker.domain.models.Playlist
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistViewModel(
    var playlistsInteractor: PlaylistsInteractor,
    var playlistsTrackInteractor: PlaylistsTrackInteractor
): ViewModel() {

    private val tracksMinutesCalcLiveData = MutableLiveData<String>()
    fun observeTracksMinutesCalcLiveData(): LiveData<String> = tracksMinutesCalcLiveData

    private val tracksLiveData = MutableLiveData<List<Track>>()
    fun observeTracksLiveData(): LiveData<List<Track>> = tracksLiveData

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    fun mapStringToPlaylist(playlistString: String): Playlist {
        return playlistsInteractor.mapStringToPlaylist(playlistString)
    }

    fun calcMinutesTracks(tracksIds: List<Int>) {
        viewModelScope.launch {
            val tracks: List<Track> = playlistsTrackInteractor.getTracksByIds(tracksIds).first()
            var tracksMinutesCalc: Long = 0
            for (track in tracks) {
                tracksMinutesCalc += track.trackTimeMillis
            }
            val minutes: Int = tracksMinutesCalc.toInt()/60000
            tracksMinutesCalcLiveData.postValue(
                if (minutes % 100 < 10 || minutes % 100 > 19) {
                    when (minutes % 10) {
                        1 -> {
                            "$minutes минута"
                        }
                        2, 3, 4 -> {
                            "$minutes минуты"
                        }
                        else -> {
                            "$minutes минут"
                        }
                    }
                } else {
                    "$minutes минут"
                }
            )
        }
    }

    fun calcNumberTracks(tracksNumber: Int): String {
        return if (tracksNumber % 100 < 10 || tracksNumber % 100 > 19) {
            when (tracksNumber % 10) {
                1 -> {
                    "$tracksNumber трек"
                }
                2,3,4 -> {
                    "$tracksNumber трека"
                }
                else -> {
                    "$tracksNumber треков"
                }
            }
        } else {
            "$tracksNumber треков"
        }
    }

    fun getPlaylistTracks(tracksIds: List<Int>) {
        viewModelScope.launch {
            val tracks: List<Track> = playlistsTrackInteractor.getTracksByIds(tracksIds).first()
            tracksLiveData.postValue(tracks)
        }
    }

    private fun deleteTrackFromDB(track: Track) {
        viewModelScope.launch {
            val playlists = playlistsInteractor.getPlaylists().first()
            var isUsed: Boolean = false
            for (playlist in playlists!!) {
                if (playlist.playlistTracks.contains(track.trackId)) {
                    isUsed = true
                }
            }
            if (!isUsed) {
                playlistsTrackInteractor.deleteTrackRow(track.trackId)
            }
        }
    }

    fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            // delete trackId from playlist
            val resultCode = playlistsInteractor.updatePlaylistRow(playlist, track.trackId, true).first()
            if (resultCode == 0) {
                // post data to update UI on playlist view
                playlistStateLiveData.postValue(
                    PlaylistState.Changed(
                        Playlist(
                            playlist.playlistTitle,
                            playlist.playlistDescription,
                            playlist.playlistCoverLocalUri,
                            playlist.playlistTracks - track.trackId,
                            playlist.playlistTracksNumber - 1
                        )
                    )
                )
                // get trackIds from all playlists if trackId in list pass else delete from DB
                deleteTrackFromDB(track)
            }
        }
    }
}