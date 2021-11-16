package com.example.moviezam.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.views.adapters.SongCardAdapter
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class SongsViewModel : ViewModel() {
    private val repo = SongRepository()
    var songList = mutableListOf<SongCard>()
    var job: Job? = null

    suspend fun loadSongsByPrefix(prefix: String, adapter: SongCardAdapter): MutableList<SongCard> {
        val allSongs = mutableListOf<SongCard>()
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            var pageNum = 1
            var songsPerPage = repo.getSongsPageByName(prefix, pageNum)
            songList.clear()
            val loading = launch {
                while (songsPerPage.isNotEmpty()) {
                    pageNum++
                    allSongs += songsPerPage
                    songList.addAll(songsPerPage)
                    val update = launch(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                    update.join()
                    songsPerPage = repo.getSongsPageByName(prefix, pageNum)
                }
            }
            loading.join()
        }
        return allSongs
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}