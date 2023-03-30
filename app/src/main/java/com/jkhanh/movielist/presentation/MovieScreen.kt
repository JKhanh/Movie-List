package com.jkhanh.movielist.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jkhanh.movielist.R
import com.jkhanh.movielist.model.Search
import com.jkhanh.movielist.utils.items

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MovieScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    var searchKey by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val movies = viewModel.movieList.collectAsLazyPagingItems()

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "List Film")
                Image(
                    painter = painterResource(id = R.drawable.baseline_people_24),
                    contentDescription = null
                )
            }

            OutlinedTextField(value = searchKey, onValueChange = {
                searchKey = it
            }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.searchMovie(searchKey)
                        keyboardController?.hide()
                    }
                ))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 5.dp)
            ) {
                items(
                    items = movies,
                    key = {it.imdbID}
                ) {
                    if (it != null) {
                        MovieItem(movie = it)
                    }
                }
            }

            when (val state = movies.loadState.refresh) {
                is LoadState.Loading -> {
                    Spacer(modifier = Modifier.width(50.dp))
                    CircularProgressIndicator()
                }
                else -> {}
            }
            when (val state = movies.loadState.append) {
                is LoadState.Loading -> {
                    Spacer(modifier = Modifier.width(50.dp))
                    CircularProgressIndicator()
                }
                else -> {}
            }
        }

}

@Composable
fun MovieItem(movie: Search) {
    Box(
        modifier = Modifier
            .width(170.dp)
            .padding(horizontal = 10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.Poster)
                .build(),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier.width(170.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = movie.Title,
                modifier = Modifier.width(120.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = movie.Year)
        }
    }
}

@Preview
@Composable
fun MovieScreenPreview() {
    MovieScreen()
}