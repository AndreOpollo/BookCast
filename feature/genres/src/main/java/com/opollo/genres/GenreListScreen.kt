package com.opollo.genres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opollo.domain.model.Book
import com.opollo.genres.components.BooksGrid
import com.opollo.genres.components.GenreTopBar

@Composable
fun GenreListScreen(
    modifier:Modifier = Modifier,
    viewModel: GenreViewModel = hiltViewModel(),
    genre:String,
    onBackClick:()->Unit,
    onBookClick:(Book)->Unit,
    onFavoriteToggle:(Book)->Unit
){
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(genre) {
        viewModel.getBooksByGenre(genre)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GenreTopBar(
            genreName = genre,
            bookCount = state.listByGenre.size,
            onBackClick = onBackClick
        )
        BooksGrid(
            books = state.listByGenre,
            onBookClick = onBookClick,
            onFavoriteToggle = onFavoriteToggle
        )

    }

}