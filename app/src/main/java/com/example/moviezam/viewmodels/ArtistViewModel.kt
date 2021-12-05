package com.example.moviezam.viewmodels

import androidx.lifecycle.*
import com.example.moviezam.models.Resource
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistViewModel {

    private val repo = ArtistRepository()
    var job: Job? = null

    fun loadArtist(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getArtistById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }

    suspend fun loadArtistsByPrefix(prefix: String, pageNum: Int): MutableList<ArtistCard> {
        job?.cancel()
        var artistsPerPage: List<ArtistCard> = emptyList()

        job = CoroutineScope(Dispatchers.IO).launch {
            artistsPerPage = repo.getArtistsByName(prefix, pageNum)
        }
        job!!.join()

        return artistsPerPage.toMutableList()
    }
}
