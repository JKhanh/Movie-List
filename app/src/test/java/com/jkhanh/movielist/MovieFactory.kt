package com.jkhanh.movielist

import com.jkhanh.movielist.model.MovieResponse
import com.jkhanh.movielist.model.Search

class MovieFactory {

    fun createMovie(): MovieResponse =
        MovieResponse("movie 1",
            listOf(Search("image url", "title", "type", "2024", "oscar"),
                Search("another url", "best picture", "type also", "1999", "it")),
            "more")

    fun createLastMovieResponse(): MovieResponse =
        MovieResponse("movie last", emptyList(), "more")
}