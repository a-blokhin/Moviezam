package com.example.moviezam.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM artist_table ORDER BY insert_time")
    fun getArtists(): List<ArtistCard>

    @Query("SELECT * FROM film_table ORDER BY insert_time")
    fun getFilms(): List<FilmCard>

    @Query("SELECT * FROM song_table ORDER BY insert_time")
    fun getSongs(): List<SongCard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artistCard: ArtistCard) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filmCard: FilmCard) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songCard: SongCard) : Long
}