package com.example.moviezam.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviezam.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

class FilmRepository {

    suspend fun getFilmById(id: Int): Response<Film> {
        return Moviezam.api.getFilmById(id)
    }

    suspend fun getFilmCardById(id: Int): FilmCard? {
        return Moviezam.api.getFilmCardById(id).execute().body()
    }

    suspend fun getFilmsPageByName(name: String, pageNum: Int): List<FilmCard> = withContext(Dispatchers.IO) {
        return@withContext Moviezam.api.getFilmsByName(name, pageNum).execute().body()
            ?: emptyList()
    }

    fun getSearchResultsStream(query: String): Flow<PagingData<FilmCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FilmsDataSource(this, query) }
        ).flow
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}