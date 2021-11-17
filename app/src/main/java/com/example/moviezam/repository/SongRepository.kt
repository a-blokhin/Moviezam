package com.example.moviezam.repository

import com.example.moviezam.models.Artist
import com.example.moviezam.models.Song
import com.example.moviezam.models.MoviezamApi
import com.example.moviezam.models.SongCard
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class SongRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://95.163.180.8:8081")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val moviezamApi = retrofit.create(MoviezamApi::class.java)

    suspend fun getSongById(id: Int): Response<Song> {
        return moviezamApi.getSongById(id)
    }

    suspend fun getSongsPageByName(name: String, pageNum: Int): List<SongCard> = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getSongsByName(name, pageNum).execute().body()
            ?: emptyList()
    }
}