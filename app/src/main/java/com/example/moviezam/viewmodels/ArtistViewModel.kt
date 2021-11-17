package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import com.example.moviezam.models.Artist
import androidx.lifecycle.*
import com.example.moviezam.models.Resource
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import com.example.moviezam.views.adapters.ArtistCardAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistViewModel {

    // TODO
    private val repo = ArtistRepository()
    var job: Job? = null
    val lastPageLoaded = 1

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
        var artistsPerPage: MutableList<ArtistCard> = emptyList<ArtistCard>().toMutableList()

        job = CoroutineScope(Dispatchers.IO).launch {
            artistsPerPage = repo.getArtistsByName(prefix, pageNum) as MutableList<ArtistCard>
        }
        job!!.join()

        return artistsPerPage
    }
}
