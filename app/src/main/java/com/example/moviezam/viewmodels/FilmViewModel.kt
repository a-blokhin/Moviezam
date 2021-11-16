package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.liveData
import com.example.moviezam.models.Film
import com.example.moviezam.models.Resource
import com.example.moviezam.repository.ArtistRepository
import com.example.moviezam.repository.FilmRepository
import kotlinx.coroutines.Dispatchers

class FilmViewModel {

    private val repo = FilmRepository()

    fun loadFilm(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getFilmById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }
}