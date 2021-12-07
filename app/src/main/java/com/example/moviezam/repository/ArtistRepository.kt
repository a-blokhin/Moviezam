package com.example.moviezam.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviezam.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse

import retrofit2.converter.gson.GsonConverterFactory

class ArtistRepository {
    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getArtistById(id: Int): Response<Artist> {
       return moviezamApi.getArtistById(id)
    }

    suspend fun getArtistCardById(id: Int): ArtistCard? {
        return Moviezam.api.getArtistCardById(id).execute().body()
    }

    suspend fun getArtistsPageByName(name: String, pageNum: Int): List<ArtistCard> = withContext(Dispatchers.IO) {
        return@withContext Moviezam.api.getArtistsByName(name, pageNum).execute().body()
            ?: emptyList()
    }

    fun getSearchResultsStream(query: String): Flow<PagingData<ArtistCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArtistsDataSource(this, query) }
        ).flow
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}