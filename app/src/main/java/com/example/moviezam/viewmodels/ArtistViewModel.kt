package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import com.example.moviezam.models.Artist
import com.example.moviezam.repository.ArtistRepository

class ArtistViewModel {

    private val repo = ArtistRepository()

    suspend fun loadArtist(id: Int): Artist {
        val artist = repo.getArtistById(id)
        return artist
    }
}