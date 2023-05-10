package com.vaiwork.playlistmaker.domain.impl

import com.vaiwork.playlistmaker.domain.api.SharedPreferenceInteractor
import com.vaiwork.playlistmaker.domain.api.SharedPreferenceRepository
import com.vaiwork.playlistmaker.domain.models.Track

class SharedPreferenceInteractorImpl(
    private val sharedPreferenceRepository: SharedPreferenceRepository
): SharedPreferenceInteractor {
    override fun addTrack(dto: Track, sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String) {
        sharedPreferenceRepository.addTrack(dto, sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
    }

    override fun getTracks(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String): ArrayList<Track> {
        return sharedPreferenceRepository.getTracks(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
    }

    override fun clear(sharedPreferenceName: String, sharedPreferenceMode: Int, sharedPreferenceKey: String) {
        sharedPreferenceRepository.clear(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey)
    }

    override fun getBoolean(
        sharedPreferenceName: String,
        sharedPreferenceMode: Int,
        sharedPreferenceKey: String,
        defaultValue: Boolean
    ): Boolean {
        return sharedPreferenceRepository.getBoolean(sharedPreferenceName, sharedPreferenceMode, sharedPreferenceKey, defaultValue)
    }
}