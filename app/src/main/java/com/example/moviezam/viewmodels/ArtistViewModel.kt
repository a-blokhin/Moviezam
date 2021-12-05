package com.example.moviezam.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.moviezam.models.*
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistViewModel {

    private val repo = ArtistRepository()
    var job: Job? = null
    var favJob: Job? = null

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

    suspend fun getFavourite(id: Int, context: Context?): FavouriteEntity? {
        favJob?.cancel()
        var fav : FavouriteEntity? = null
        favJob = CoroutineScope(Dispatchers.IO).launch {
            fav = AppDatabase.getInstance(context)?.favDao?.getByType(id.toLong(), getType(
                Type.ARTIST)
            )
        }
        favJob!!.join()

        return fav
    }

    suspend fun addFavourite(fav: FavouriteEntity, context: Context?) {
        favJob?.cancel()
        favJob = CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(context)?.favDao?.insert(fav)
        }
        favJob!!.join()
    }

    suspend fun deleteFavourite(fav: FavouriteEntity, context: Context?) {
        favJob?.cancel()
        favJob = CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(context)?.favDao?.delete(fav.id, getType(Type.ARTIST))
        }
        favJob!!.join()
    }
}
