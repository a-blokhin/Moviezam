package com.example.moviezam.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.*
import com.example.moviezam.models.*
import com.example.moviezam.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import com.example.moviezam.views.adapters.ArtistCardAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavouriteViewModel {

    // TODO
    private val repo = ArtistRepository()
    var job: Job? = null

    fun loadFavourite() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(Resource.success(Favourite(
            listOf(
                FavouriteEntity(105808,
                    "Harry Potter and the Half-Blood Prince (Original Motion Picture Soundtrack)",
                    "https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/c4/3c/66/c43c66c5-df9c-7021-7df0-04c1678c0ed3/794043915260.jpg/88x88bb.jpeg",
                    "song")
            )
        )))
    }

}
