package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.dto.TrackSearchRequest
import com.vaiwork.playlistmaker.data.dto.TrackSearchResponse
import com.vaiwork.playlistmaker.domain.api.Resource
import com.vaiwork.playlistmaker.domain.api.TracksRepository
import com.vaiwork.playlistmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): Resource<ArrayList<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        var result: ArrayList<Track> = arrayListOf()
        when (response.resultCode) {
            200 -> {
                for (it in (response as TrackSearchResponse).results) {
                    result.add(Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTime,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    ))
                }
                return Resource.Success(result)
            }
            -1 -> {
                return Resource.Error("Проверьте подключение к интернету")
            }
            else -> {
                return Resource.Error("Ошибка сервера")
            }
        }
    }

}