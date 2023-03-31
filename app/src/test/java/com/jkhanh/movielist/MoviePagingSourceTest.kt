package com.jkhanh.movielist

import androidx.paging.PagingSource
import com.jkhanh.movielist.data.paging.MoviePagingSource
import com.jkhanh.movielist.data.remote.MovieService
import com.jkhanh.movielist.model.Search
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviePagingSourceTest {
    private val service: MovieService = mockk(relaxed = true)
    private val movieFactory = MovieFactory()
    private val mockMovie = movieFactory.createMovie()
    private lateinit var pagingSource: MoviePagingSource

    @Before
    fun setup() {
        coEvery { service.searchMovieByName(s = "", page = 1) } returns mockMovie
    }

    private suspend fun loadRefreshPagingData() =
        pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

    private suspend fun loadAppendPagingData() =
        pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 10,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

    @Test
    fun `Given PagingSource, When search movie, Then call service search movie by name`() =
        runTest {
            val searchTitle = "movie name"
            pagingSource = MoviePagingSource(service, searchTitle)
            loadRefreshPagingData()
            coVerify { service.searchMovieByName(s = searchTitle, page = any()) }
        }

    @Test
    fun `Given service return data, When search movie, Then return Page data`() = runTest {
        pagingSource = MoviePagingSource(service, "")
        assertEquals(
            PagingSource.LoadResult.Page(
                data = mockMovie.searches,
                prevKey = null,
                nextKey = 2
            ),
            loadRefreshPagingData()
        )
    }

    @Test
    fun `Given Service return error, When search movie, Then return Load result Error`() = runTest {
        val exception = Exception()
        coEvery { service.searchMovieByName(s = any(), page = any()) } throws exception

        pagingSource = MoviePagingSource(service, "")
        assertEquals(
            PagingSource.LoadResult.Error<Int, Search>(exception),
            loadRefreshPagingData()
        )
    }

    @Test
    fun `Given Service return data page 1, When search movie, Then return Load Page with prevKey null`() =
        runTest {
            pagingSource = MoviePagingSource(service, "")

            val actual = loadRefreshPagingData() as PagingSource.LoadResult.Page

            assertNull(
                actual.prevKey
            )
        }

    @Test
    fun `Given Service return data page 10, When search movie, Then return Load Page with prevKey 9`() =
        runTest {
            coEvery { service.searchMovieByName(s = any(), page = any()) } returns movieFactory.createLastMovieResponse()
            pagingSource = MoviePagingSource(service, "")
            val actual = loadAppendPagingData() as PagingSource.LoadResult.Page

            assertEquals(
                9,
                actual.prevKey
            )
        }

    @Test
    fun `Given Service return data page 1, When search movie, Then return Load Page with nextKey 2`() =
        runTest {
            pagingSource = MoviePagingSource(service, "")

            val actual = loadRefreshPagingData() as PagingSource.LoadResult.Page

            assertEquals(2, actual.nextKey)
        }

    @Test
    fun `Given Service return last data page, When search movie, Then return Load Page with nextKey null`() =
        runTest {
            coEvery {
                service.searchMovieByName(
                    s = any(),
                    page = any()
                )
            } returns movieFactory.createLastMovieResponse()

            pagingSource = MoviePagingSource(service, "")

            val actual = loadRefreshPagingData() as PagingSource.LoadResult.Page

            assertNull(actual.nextKey)
        }
}