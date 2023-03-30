package com.jkhanh.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.jkhanh.movielist.model.Search
import com.jkhanh.movielist.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    private var _movieList: MutableStateFlow<PagingData<Search>> = MutableStateFlow(PagingData.empty())
    val movieList: StateFlow<PagingData<Search>> get() = _movieList

    fun searchMovie(searchKey: String) {
        viewModelScope.launch {
            repository.searchMovieByName(searchKey).collect {
                _movieList.tryEmit(it)
            }
        }
    }
}