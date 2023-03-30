package com.jkhanh.movielist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jkhanh.movielist.data.paging.MoviePagingSource
import com.jkhanh.movielist.model.Search
import com.jkhanh.movielist.data.remote.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
): MovieRepository {
    override fun searchMovieByName(searchKey: String): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                MoviePagingSource(
                    movieService, searchKey
                )
            }
        ).flow
    }
}