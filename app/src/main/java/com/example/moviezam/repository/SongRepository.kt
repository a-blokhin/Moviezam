package com.example.moviezam.repository

import android.content.res.Resources
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviezam.R
import com.example.moviezam.models.Artist
import com.example.moviezam.models.Song
import com.example.moviezam.models.MoviezamApi
import com.example.moviezam.models.SongCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class SongRepository {
    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getSongById(id: Int): Response<Song> {
        return moviezamApi.getSongById(id)
    }

//    @Deprecated("use method returning Response<SongCard> in future")
    suspend fun getSongCardById(id: Int): SongCard = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getSongCardById(id).execute().body()
            ?: error("Not found song")
    }

    suspend fun getSongsPageByName(name: String, pageNum: Int): List<SongCard> = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getSongsByName(name, pageNum).execute().body()
            ?: emptyList()
    }

    fun getSearchResultsStream(query: String): Flow<PagingData<SongCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SongDataSource(this, query) }
        ).flow
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}