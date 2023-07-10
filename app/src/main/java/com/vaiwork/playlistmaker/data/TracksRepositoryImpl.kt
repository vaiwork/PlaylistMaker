package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.util.Resource
import com.vaiwork.playlistmaker.data.dto.TrackSearchRequest
import com.vaiwork.playlistmaker.data.dto.TrackSearchResponse
import com.vaiwork.playlistmaker.domain.api.TracksRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        val result: ArrayList<Track> = arrayListOf()
        when (response.resultCode) {
            200 -> {
                for (it in (response as TrackSearchResponse).results) {
                    result.add(
                        Track(
                            it.trackId,
                            when (it.trackName) {
                                null -> ""
                                else -> it.trackName
                            },
                            when (it.artistName) {
                                null -> ""
                                else -> it.artistName
                            },
                            it.trackTimeMillis,
                            when (it.artworkUrl100) {
                                null -> ""
                                else -> it.artworkUrl100
                            },
                            when (it.collectionName) {
                                null -> ""
                                else -> it.collectionName
                            },
                            when (it.releaseDate) {
                                null -> ""
                                else -> it.releaseDate
                            },
                            when (it.primaryGenreName) {
                                null -> ""
                                else -> it.primaryGenreName
                            },
                            when (it.country) {
                                null -> ""
                                else -> it.country
                            },
                            when (it.previewUrl) {
                                null -> ""
                                else -> it.previewUrl
                            }
                        )
                    )
                    emit(Resource.Success(result))
                }
            }

            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

}