package com.example.moviezam.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.LoadingInfo
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.ArtistRepository
import com.example.moviezam.repository.FilmRepository
import com.example.moviezam.repository.SongRepository
import com.example.moviezam.views.ui.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {
    private var songRepo = SongRepository()
    private var filmRepo = FilmRepository()
    private var artistRepo = ArtistRepository()

    private var currentSongsQuery: String? = null
    private var currentArtistsQuery: String? = null
    private var currentFilmsQuery: String? = null

    private var currentSongSearchResult: Flow<PagingData<SongCard>>? = null
    private var currentArtistSearchResult: Flow<PagingData<ArtistCard>>? = null
    private var currentFilmSearchResult: Flow<PagingData<FilmCard>>? = null

    fun searchSongs(query: String): Flow<PagingData<SongCard>> {
        val lastResult = currentSongSearchResult
        if (query == currentSongsQuery && lastResult != null) {
            return lastResult
        }
        currentSongsQuery = query
        val newResult: Flow<PagingData<SongCard>> =
            songRepo.getSearchResultsStream(query)
        currentSongSearchResult = newResult
        return newResult
    }

    fun searchFilms(query: String): Flow<PagingData<FilmCard>> {
        val lastResult = currentFilmSearchResult
        if (query == currentFilmsQuery && lastResult != null) {
            return lastResult
        }
        currentFilmsQuery = query
        val newResult: Flow<PagingData<FilmCard>> =
            filmRepo.getSearchResultsStream(query)
        currentFilmSearchResult = newResult
        return newResult
    }

    fun searchArtists(query: String): Flow<PagingData<ArtistCard>> {
        val lastResult = currentArtistSearchResult
        if (query == currentArtistsQuery && lastResult != null) {
            return lastResult
        }
        currentArtistsQuery = query
        val newResult: Flow<PagingData<ArtistCard>> =
            artistRepo.getSearchResultsStream(query)
        currentArtistSearchResult = newResult
        return newResult
    }


//    fun uploadSongList(text: String = songLoadingInfo.currSearchText) {
//        if (!songLoadingInfo.hasPagesToLoad) return
//
//        if (text != songLoadingInfo.currSearchText) {
//            songList.clear()
//            songLoadingInfo = LoadingInfo(1, text, true)
//        }
//        songJob?.cancel()
//
//        songJob = CoroutineScope(Dispatchers.IO).launch {
//            val loadedList = songsViewModel.loadSongsByPrefix(text, songLoadingInfo.currPageNumber)
//
//            if (loadedList.isEmpty()) {
//                songLoadingInfo.hasPagesToLoad = false
//            } else {
//                songList.addAll(loadedList)
//            }
//            songLoadingInfo.currPageNumber++
//        }
//    }

//    fun uploadFilmList(text: String = filmLoadingInfo.currSearchText) {
//        if (!filmLoadingInfo.hasPagesToLoad) return
//
//        if (text != filmLoadingInfo.currSearchText) {
//            filmList.clear()
//            filmLoadingInfo = LoadingInfo(1, text, true)
//        }
//        filmJob?.cancel()
//
//        filmJob = CoroutineScope(Dispatchers.IO).launch {
//            val loadedList = filmViewModel.loadFilmsByPrefix(text, filmLoadingInfo.currPageNumber)
//
//            if (loadedList.isEmpty()) {
//                filmLoadingInfo.hasPagesToLoad = false
//            } else {
//                filmList.addAll(loadedList)
//            }
//            filmLoadingInfo.currPageNumber++
//        }
//    }

//    fun uploadArtistList(text: String = artistLoadingInfo.currSearchText) {
//        if (!artistLoadingInfo.hasPagesToLoad) return
//
//        if (text != artistLoadingInfo.currSearchText) {
//            artistList.clear()
//            artistLoadingInfo = LoadingInfo(1, text, true)
//        }
//        artistJob?.cancel()
//
//        artistJob = CoroutineScope(Dispatchers.IO).launch {
//            val loadedList =
//                artistsViewModel.loadArtistsByPrefix(text, artistLoadingInfo.currPageNumber)
//
//            if (loadedList.isEmpty()) {
//                artistLoadingInfo.hasPagesToLoad = false
//            } else {
//                artistList.addAll(loadedList)
//            }
//            artistLoadingInfo.currPageNumber++
//        }
//    }

}