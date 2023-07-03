package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.util.Resource
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.api.TracksRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    /*
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            when (val response = repository.searchTracks(expression)) {
                is Resource.Success<*> -> consumer.consume(response.data, null)
                is Resource.Error<*> -> consumer.consume(null, response.message)
            }
        }
        t.start()
    }
    */
    override fun searchTracks(expression: String): Flow<Pair<ArrayList<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(result.data, result.message)
                }
            }
        }

    }
}