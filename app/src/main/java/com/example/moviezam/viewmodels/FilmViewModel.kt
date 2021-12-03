package com.example.moviezam.viewmodels

import androidx.lifecycle.liveData
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.Resource
import com.example.moviezam.repository.FilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FilmViewModel {

    private val repo = FilmRepository()
    private var job: Job? = null

    fun loadFilm(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getFilmById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }

    suspend fun loadFilmsByPrefix(prefix: String, pageNum: Int): MutableList<FilmCard> {
        job?.cancel()
        var filmsPerPage: List<FilmCard> = emptyList()

        job = CoroutineScope(Dispatchers.IO).launch {
            filmsPerPage = repo.getFilmsPageByName(prefix, pageNum)
        }
        job!!.join()

        return filmsPerPage.toMutableList()
    }
}