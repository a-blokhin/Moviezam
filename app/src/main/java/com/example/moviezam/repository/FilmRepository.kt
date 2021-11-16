package com.example.moviezam.repository

import com.example.moviezam.models.Artist
import com.example.moviezam.models.Film
import com.example.moviezam.models.MoviezamApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class FilmRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://95.163.180.8:8081")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getFilmById(id: Int): Response<Film> {
        return moviezamApi.getFilmById(id)
    }

}