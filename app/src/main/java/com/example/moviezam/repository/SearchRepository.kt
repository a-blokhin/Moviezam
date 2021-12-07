package com.example.moviezam.repository

import androidx.annotation.WorkerThread
import com.example.moviezam.models.ArtistCard
import com.example.moviezam.models.FilmCard
import com.example.moviezam.models.SearchDao
import com.example.moviezam.models.SongCard
import kotlinx.coroutines.flow.Flow

class SearchRepository(private val searchDao: SearchDao) {

    val songsHistory: List<SongCard> = searchDao.getSongs()
    val artistsHistory: List<ArtistCard> = searchDao.getArtists()
    val filmsHistory: List<FilmCard> = searchDao.getFilms()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSong(songCard: SongCard) {
        searchDao.insert(songCard)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertArtist(artistCard: ArtistCard) {
        searchDao.insert(artistCard)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFilm(filmCard: FilmCard) {
        searchDao.insert(filmCard)
    }
}