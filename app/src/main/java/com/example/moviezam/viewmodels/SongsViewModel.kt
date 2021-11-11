package com.example.moviezam.viewmodels

import com.example.moviezam.models.Artist
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.ArtistRepository
import com.example.moviezam.repository.SongRepository

class SongsViewModel {
    private val repo = SongRepository()

    suspend fun loadSongs(ids: List<Int>): List<SongCard> {
        val songsList = List(ids.size) { i -> repo.getSongById(i) }
        return songsList
    }
}