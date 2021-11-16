package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.liveData
import com.example.moviezam.models.Resource
import com.example.moviezam.models.Song
import com.example.moviezam.repository.ArtistRepository
import com.example.moviezam.repository.SongRepository
import kotlinx.coroutines.Dispatchers

class SongViewModel {

    private val repo = SongRepository()

    fun loadSong(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getSongById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }
}