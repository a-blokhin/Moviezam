package com.example.moviezam.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviezam.models.FilmCard
import retrofit2.HttpException
import java.io.IOException

class FilmsDataSource(private val repository: FilmRepository,
                     private val query: String
): PagingSource<Int, FilmCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmCard> {
        val position = params.key ?: 1

        return try {
            val response = repository.getFilmsPageByName(query, position)

            LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position -1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FilmCard>): Int? {
        TODO("Not yet implemented")
    }

}