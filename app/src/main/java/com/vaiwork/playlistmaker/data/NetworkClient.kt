package com.vaiwork.playlistmaker.data

import com.vaiwork.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}