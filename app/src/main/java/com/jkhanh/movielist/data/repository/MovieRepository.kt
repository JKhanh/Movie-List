package com.jkhanh.movielist.data.repository

import androidx.paging.PagingData
import com.jkhanh.movielist.model.Search
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun searchMovieByName(searchKey: String): Flow<PagingData<Search>>
}