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
    private var job: Job? = null

    suspend fun loadSongsByPrefix(prefix: String, adapter: SongCardAdapter, adapterList: MutableList<SongCard>) {
        job?.cancel()

        job = CoroutineScope(Dispatchers.IO).launch {
            adapterList.clear()
            val update = CoroutineScope(Dispatchers.Main).launch {
                adapter.notifyDataSetChanged()
            }
            update.join()

            var pageNum = 1
            var songsPerPage = repo.getSongsPageByName(prefix, pageNum)

            val loading = launch {
                while (songsPerPage.isNotEmpty()) {
                    pageNum++
                    val prevListSize = adapterList.size
                    adapterList.addAll(songsPerPage)

                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.notifyItemRangeInserted(prevListSize, songsPerPage.size)
                    }
                    songsPerPage = repo.getSongsPageByName(prefix, pageNum)
                }
            }
            loading.join()
        }
        job!!.join()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}