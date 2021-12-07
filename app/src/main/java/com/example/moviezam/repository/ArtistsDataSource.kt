package com.example.moviezam.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviezam.models.ArtistCard
import retrofit2.HttpException
import java.io.IOException

class ArtistsDataSource(private val repository: ArtistRepository,
                     private val query: String
): PagingSource<Int, ArtistCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistCard> {
        val position = params.key ?: 1

        return try {
            val response = repository.getArtistsPageByName(query, position)

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

    override fun getRefreshKey(state: PagingState<Int, ArtistCard>): Int? {
        TODO("Not yet implemented")
    }

}