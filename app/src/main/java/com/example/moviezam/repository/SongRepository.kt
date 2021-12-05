package com.example.moviezam.repository

import com.example.moviezam.models.Moviezam
import com.example.moviezam.models.Song
import com.example.moviezam.models.SongCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class SongRepository {

    suspend fun getSongById(id: Int): Response<Song> {
        return Moviezam.api.getSongById(id)
    }

    suspend fun getSongsPageByName(name: String, pageNum: Int): List<SongCard> = withContext(Dispatchers.IO) {
        return@withContext Moviezam.api.getSongsByName(name, pageNum).execute().body()
            ?: emptyList()
    }

}