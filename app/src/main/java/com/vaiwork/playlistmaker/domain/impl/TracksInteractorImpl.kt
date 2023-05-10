package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.api.Resource
import com.vaiwork.playlistmaker.domain.api.TracksInteractor
import com.vaiwork.playlistmaker.domain.api.TracksRepository

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            when (val response = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume(response.data , null)
                is Resource.Error -> consumer.consume(null, response.message)
            }
        }
        t.start()
    }
}