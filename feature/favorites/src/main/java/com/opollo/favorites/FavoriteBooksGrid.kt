package com.opollo.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.domain.model.Book


@Composable
fun BooksGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    viewModel: FavoritesViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = books,
            key = { book -> book.id }
        ) { book ->
            val isFavorite by viewModel.isFavorite(bookId = book.id).collectAsState(initial = false)

            FavoriteBookCard(
                book = book,
                isFavorite = isFavorite,
                onCardClick = { onBookClick(book) },
                onFavoriteToggle = { viewModel.toggleFavorite(book.id,isFavorite) }
            )
        }
    }
}