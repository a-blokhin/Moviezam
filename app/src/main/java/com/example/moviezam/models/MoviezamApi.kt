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
    fun getFilmById(
        @Query("id") id: Int
    ): Response<Artist>

    @GET("/song")
    fun getSongById(
        @Query("id") id: Int
    ): Response<Artist>
}