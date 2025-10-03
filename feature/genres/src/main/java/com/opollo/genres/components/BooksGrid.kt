package com.opollo.genres.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.domain.model.Book
import com.opollo.genres.GenreViewModel

@Composable
fun BooksGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    onFavoriteToggle: (Book) -> Unit,
    viewModel: GenreViewModel
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
            GenreBookCard(
                book = book,
                onCardClick = { onBookClick(book) },
                onFavoriteToggle = { onFavoriteToggle(book) },
                viewModel = viewModel
            )
        }
    }
}