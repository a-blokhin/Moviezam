package com.example.moviezam.repository

import android.content.res.Resources
import com.example.moviezam.R
import com.example.moviezam.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

class FilmRepository {
    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getFilmById(id: Int): Response<Film> {
        return moviezamApi.getFilmById(id)
    }

    suspend fun getFilmsByName(name: String, page: Int): List<FilmCard> = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getFilmsByName(name, page).execute().body()
            ?: emptyList()
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}