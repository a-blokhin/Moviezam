package com.example.moviezam.models

import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviezamApi {

    @GET("/artist")
    suspend fun getArtistById(
        @Query("id") id: Int
    ): Response<Artist>

    @GET("/artist")
    fun getArtistCardById(
        @Query("id") id: Int
    ): Call<ArtistCard>

    @GET("/film")
    suspend fun getFilmById(
        @Query("id") id: Int
    ): Response<Film>

    @GET("/film")
    fun getFilmCardById(
        @Query("id") id: Int
    ): Call<FilmCard>

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

    @GET("/find_film")
    fun getFilmsByName(
        @Query("search") name: String,
        @Query("page") pageNumber: Int
    ): Call<List<FilmCard>>
}

class Moviezam {
    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var api: MoviezamApi = retrofit.create(MoviezamApi::class.java)
    }
}