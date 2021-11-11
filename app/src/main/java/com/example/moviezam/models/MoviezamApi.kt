package com.example.moviezam.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviezamApi {
    @GET("/artist")
    fun getArtistById(
        @Query("id") id: Int
    ): Call<Artist>

    @GET("/song")
    fun getSongById(
        @Query("id") id: Int
    ): Call<SongCard>

    @GET("/find_song")
    fun getSongsByName(
        @Query("search") name: String,
        @Query("page") pageNumber: Int
    ): Call<List<SongCard>>
}