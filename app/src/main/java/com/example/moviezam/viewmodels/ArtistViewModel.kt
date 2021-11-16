package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import com.example.moviezam.models.Artist
import androidx.lifecycle.liveData
import com.example.moviezam.models.Resource
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import com.example.moviezam.views.adapters.ArtistCardAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistViewModel {

    // TODO
    private val repo = ArtistRepository()
    var job: Job? = null
    val artistList = mutableListOf<ArtistCard>()

    fun loadArtist(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getArtistById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }

    suspend fun loadArtistsByPrefix(prefix: String, adapter: ArtistCardAdapter): MutableList<ArtistCard> {
        val allArtists = mutableListOf<ArtistCard>()
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            var pageNum = 1
            var artistsPerPage = repo.getArtistsByName(prefix, pageNum)
            artistList.clear()
            val loading = launch {
                while (artistsPerPage.isNotEmpty()) {
                    pageNum++
                    allArtists += artistsPerPage
                    artistList.addAll(artistsPerPage)
                    val update = launch(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                    update.join()
                    artistsPerPage = repo.getArtistsByName(prefix, pageNum)
                }
            }
            loading.join()
        }
        return allArtists
    }
}
