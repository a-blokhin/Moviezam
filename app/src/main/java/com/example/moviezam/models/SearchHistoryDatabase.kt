package com.example.moviezam.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SongCard::class, FilmCard::class, ArtistCard::class], version = 1)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract val searchDao: SearchDao?

    companion object {
        private var instance: SearchHistoryDatabase? = null
        @Synchronized
        fun getInstance(context: Context?): SearchHistoryDatabase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context?): SearchHistoryDatabase {
            return Room.databaseBuilder(
                context!!,
                SearchHistoryDatabase::class.java,
                "searchHistory"
            ).allowMainThreadQueries()
                .build()
        }
    }
}