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
    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getFilmById(id: Int): Response<Film> {
        return moviezamApi.getFilmById(id)
    }

    suspend fun getFilmsPageByName(name: String, page: Int): List<FilmCard> = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getFilmsByName(name, page).execute().body()
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