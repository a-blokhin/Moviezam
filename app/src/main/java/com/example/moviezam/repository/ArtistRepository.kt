package com.example.moviezam.repository

import android.util.Log
import com.example.moviezam.models.Artist
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.Moviezam

import kotlinx.coroutines.Dispatchers
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

}