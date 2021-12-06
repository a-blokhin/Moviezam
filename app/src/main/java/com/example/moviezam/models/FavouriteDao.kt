package com.example.moviezam.models

import androidx.room.*

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM FavouriteEntity")
    fun getAll(): List<FavouriteEntity>?

    @Query("SELECT * FROM FavouriteEntity WHERE id = :id and type = :type")
    fun getByType(id: Long, type: String): FavouriteEntity?

    @Insert
    fun insert(fav: FavouriteEntity?)

    @Query("DELETE FROM FavouriteEntity WHERE id = :id and type = :type")
    fun delete(id: Long, type: String)
}