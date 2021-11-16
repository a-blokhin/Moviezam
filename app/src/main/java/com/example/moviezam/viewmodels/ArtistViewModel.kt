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
    val artistList = mutableListOf<ArtistCard>()

    fun loadArtist(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val response = repo.getArtistById(id)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(null))
        }
    }

    suspend fun loadArtistsByPrefix(prefix: String, adapter: ArtistCardAdapter, adapterList: MutableList<ArtistCard>) {
        job?.cancel()

        job = CoroutineScope(Dispatchers.IO).launch {
            adapterList.clear()
            val update = CoroutineScope(Dispatchers.Main).launch {
                adapter.notifyDataSetChanged()
            }
            update.join()

            var pageNum = 1
            var artistsPerPage = repo.getArtistsByName(prefix, pageNum)

            val loading = launch {
                while (artistsPerPage.isNotEmpty()) {
                    pageNum++
                    val prevListSize = adapterList.size
                    adapterList.addAll(artistsPerPage)

                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.notifyItemRangeInserted(prevListSize, artistsPerPage.size)
                    }

                    artistsPerPage = repo.getArtistsByName(prefix, pageNum)
                }
            }
            loading.join()
        }
        job!!.join()
    }
}
