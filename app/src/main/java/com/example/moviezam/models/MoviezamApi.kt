package com.example.moviezam.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviezamApi {
    @GET("/artist")
    fun getArtistById(
        @Query("id") id: Int
    ): Call<Artist>

    @GET("/film")
    fun getFilmById(
        @Query("id") id: Int
    ): Call<Film>

    @GET("/song")
    fun getSongById(
        @Query("id") id: Int
    ): Call<Song>
}