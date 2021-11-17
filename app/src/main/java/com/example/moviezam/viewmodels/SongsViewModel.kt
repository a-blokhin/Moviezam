package com.example.moviezam.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.views.adapters.SongCardAdapter
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class SongsViewModel : ViewModel() {
    private val repo = SongRepository()
    private var job: Job? = null

    suspend fun loadSongsByPrefix(prefix: String, pageNum: Int): MutableList<SongCard> {
        job?.cancel()
        var songsPerPage: MutableList<SongCard> = emptyList<SongCard>().toMutableList()

        job = CoroutineScope(Dispatchers.IO).launch {
            songsPerPage = repo.getSongsPageByName(prefix, pageNum) as MutableList<SongCard>
        }
        job!!.join()

        return songsPerPage
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}