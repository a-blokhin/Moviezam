package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import com.example.moviezam.models.Film
import com.example.moviezam.repository.FilmRepository

class FilmViewModel {

    private val repo = FilmRepository()

    suspend fun loadFilm(id: Int): Film {
        val film = repo.getFilmById(id)
        return film
    }
}