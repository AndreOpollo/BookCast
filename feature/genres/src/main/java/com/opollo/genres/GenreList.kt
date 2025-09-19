package com.opollo.genres

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.opollo.ui.Book
import com.opollo.ui.BookCard

@Composable
fun GenreList(books:List<Book>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(books){
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