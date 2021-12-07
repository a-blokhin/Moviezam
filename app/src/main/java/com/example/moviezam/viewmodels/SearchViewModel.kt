package com.example.moviezam.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.SongCard
import com.example.moviezam.repository.ArtistRepository
import com.example.moviezam.repository.FilmRepository
import com.example.moviezam.repository.SongRepository
import kotlinx.coroutines.flow.Flow

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
}