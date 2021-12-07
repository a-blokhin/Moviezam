package com.example.moviezam.repository

import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviezam.models.SongCard
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class SongDataSource(private val repository: SongRepository,
                     private val query: String
): PagingSource<Int, SongCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongCard> {
        val position = params.key ?: 1

        return try {
            val response = repository.getSongsPageByName(query, position)

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

    override fun getRefreshKey(state: PagingState<Int, SongCard>): Int? {
        TODO("Not yet implemented")
    }

}