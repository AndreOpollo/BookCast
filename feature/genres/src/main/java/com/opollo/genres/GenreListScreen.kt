package com.opollo.genres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun GenreListScreen(
    viewModel: GenreViewModel = hiltViewModel(),
    genre:String
){
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(genre) {
        viewModel.getBooksByGenre(genre)
    }
    GenreList(state.listByGenre)
}