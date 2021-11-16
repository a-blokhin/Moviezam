package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import com.example.moviezam.models.Artist
import androidx.lifecycle.liveData
import com.example.moviezam.models.Resource
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers

class ArtistViewModel {

    // TODO
    private val repo = ArtistRepository()

    fun loadArtist(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getArtistById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }
}
