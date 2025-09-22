package com.opollo.genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.domain.model.Book
import com.opollo.ui.BookCard

@Composable
fun GenreList(books:List<Book>){
    LazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)){
        items(books,
            key = {book->book.id}){
            book->
            BookCard(
                book = book,
                isFavorite = true,
                onCardClick = {},
                onFavoriteToggle = {},
            )
        }

    }

}