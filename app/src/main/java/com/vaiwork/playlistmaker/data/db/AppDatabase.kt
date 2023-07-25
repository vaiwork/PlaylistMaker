package com.vaiwork.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vaiwork.playlistmaker.data.db.dao.FavouriteTracksDao
import com.vaiwork.playlistmaker.data.db.dao.PlaylistsDao
import com.vaiwork.playlistmaker.data.db.entity.PlaylistEntity
import com.vaiwork.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun favouriteTracksDao(): FavouriteTracksDao

    abstract fun playlistsDao(): PlaylistsDao
}