package com.opollo.favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.domain.model.Book


@Composable
fun FavoritesScreen(
    modifier:Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBookClick:(Book)->Unit,
){
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FavoritesTopBar(
            bookCount = state.favoriteBooks.size,
        )
        when{
            state.favoriteBooks.isEmpty()->{
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No favorite books yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            else->{
                BooksGrid(
                    books = state.favoriteBooks,
                    onBookClick = onBookClick,
                    viewModel = viewModel
                )
            }
        }
    }
}