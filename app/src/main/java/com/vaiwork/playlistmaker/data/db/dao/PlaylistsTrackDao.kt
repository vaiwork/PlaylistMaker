package com.vaiwork.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.vaiwork.playlistmaker.data.db.entity.PlaylistsTrackEntity

@Dao
interface PlaylistsTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(track: List<PlaylistsTrackEntity>)

}