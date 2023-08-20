package com.vaiwork.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vaiwork.playlistmaker.data.db.entity.PlaylistsTrackEntity

@Dao
interface PlaylistsTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: List<PlaylistsTrackEntity>)


    @Query("SELECT * FROM playlists_track WHERE trackId = :trackId")
    suspend fun selectPlaylistsTrackById(trackId: Int): PlaylistsTrackEntity?

    @Query("DELETE FROM playlists_track WHERE trackId = :trackId")
    suspend fun deleteTrackRow(trackId: Int)
}