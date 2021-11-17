package com.example.moviezam.repository

import com.example.moviezam.models.Artist
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.MoviezamApi
import com.example.moviezam.models.Resource
import kotlinx.coroutines.Dispatchers
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

    suspend fun getArtistsByName(name: String, page: Int): List<ArtistCard> = withContext(Dispatchers.IO) {
        return@withContext moviezamApi.getArtistsByName(name, page).execute().body()
            ?: emptyList()
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://95.163.180.8:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}