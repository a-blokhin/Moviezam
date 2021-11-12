package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import com.example.moviezam.models.Song
import com.example.moviezam.repository.SongRepository

class SongViewModel {

    private val repo = SongRepository()

    suspend fun loadSong(id: Int): Song {
        val song = repo.getSongById(id)
        return song
    }
}