package com.example.moviezam.repository

import android.util.Log
import com.example.moviezam.models.Artist
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.Moviezam

import com.example.moviezam.models.MoviezamApi
import com.example.moviezam.models.Resource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviezam.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

class ArtistRepository {

    suspend fun getArtistById(id: Int): Response<Artist> {
       return Moviezam.api.getArtistById(id)
    }

    suspend fun getArtistsByName(name: String, page: Int): List<ArtistCard> = withContext(Dispatchers.IO) {
        return@withContext Moviezam.api.getArtistsByName(name, page).execute().body()
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