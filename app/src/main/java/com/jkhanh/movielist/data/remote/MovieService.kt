package com.jkhanh.movielist.data.remote

import com.jkhanh.movielist.BuildConfig
import com.jkhanh.movielist.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET(".")
    suspend fun searchMovieByName(
        @Query("apikey") apikey: String = BuildConfig.API_KEY,
        @Query("s") s: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int
    ): MovieResponse
}