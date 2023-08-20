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

}