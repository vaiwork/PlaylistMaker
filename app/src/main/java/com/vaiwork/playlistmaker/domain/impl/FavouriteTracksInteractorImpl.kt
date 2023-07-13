package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.db.FavouriteTracksInteractor
import com.vaiwork.playlistmaker.domain.db.FavouriteTracksRepository
import com.vaiwork.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
): FavouriteTracksInteractor {

    override fun getFavouriteTracks(): Flow<List<Track>> {
        return favouriteTracksRepository.getFavouritesTracks()
    }

    override suspend fun addTrackToFavourite(track: Track) {
        return favouriteTracksRepository.addTrackToFavourite(track)
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        return favouriteTracksRepository.deleteTrackFromFavourite(track)
    }
}