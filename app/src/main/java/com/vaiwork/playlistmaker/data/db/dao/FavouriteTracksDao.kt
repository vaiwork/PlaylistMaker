package com.vaiwork.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity

@Dao
interface FavouriteTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Query("SELECT * FROM favourite_tracks_table")
    suspend fun selectAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM favourite_tracks_table WHERE trackId =:trackId")
    suspend fun selectTrackByTrackId(trackId: Int): TrackEntity

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)
}