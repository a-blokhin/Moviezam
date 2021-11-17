package com.example.moviezam.models

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviezamApi {
    @GET("/artist")
    suspend fun getArtistById(
        @Query("id") id: Int
    ): Response<Artist>

    @GET("/film")
    suspend fun getFilmById(
        @Query("id") id: Int
    ): Response<Film>

    @GET("/song")
    suspend fun getSongById(
        @Query("id") id: Int
    ): Response<Song>

    @GET("/song")
    fun getSongCardById(
        @Query("id") id: Int
    ): Call<SongCard>

    @GET("/find_song")
    fun getSongsByName(
        @Query("search") name: String,
        @Query("page") pageNumber: Int
    ): Call<List<SongCard>>

    @GET("/find_artist")
    fun getArtistsByName(
        @Query("search") name: String,
        @Query("page") pageNumber: Int
    ): Call<List<ArtistCard>>
}