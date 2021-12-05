package com.example.moviezam.repository

import com.example.moviezam.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

class FilmRepository {

    suspend fun getFilmById(id: Int): Response<Film> {
        return Moviezam.api.getFilmById(id)
    }

    suspend fun getFilmsByName(name: String, page: Int): List<FilmCard> = withContext(Dispatchers.IO) {
        return@withContext Moviezam.api.getFilmsByName(name, page).execute().body()
            ?: emptyList()
    }

}