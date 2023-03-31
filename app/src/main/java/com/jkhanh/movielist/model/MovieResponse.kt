package com.jkhanh.movielist.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val searches: List<Search>,
    val totalResults: String
)