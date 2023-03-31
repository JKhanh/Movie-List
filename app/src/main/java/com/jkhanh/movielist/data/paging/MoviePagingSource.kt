package com.jkhanh.movielist.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jkhanh.movielist.model.Search
import com.jkhanh.movielist.data.remote.MovieService

class MoviePagingSource(
    private val movieService: MovieService,
    private val searchKey: String
): PagingSource<Int, Search>() {
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let {anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val page = params.key ?: 1
            val response = movieService.searchMovieByName(page = page, s = searchKey)

            LoadResult.Page(
                data = response.searches,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.searches.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}