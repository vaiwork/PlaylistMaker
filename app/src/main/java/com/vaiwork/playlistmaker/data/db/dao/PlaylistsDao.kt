package com.vaiwork.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlists_table")
    suspend fun selectAllPlaylists(): List<PlaylistEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylistRow(playlist: PlaylistEntity): Int
}