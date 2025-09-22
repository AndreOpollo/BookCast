package com.opollo.genres

import com.opollo.domain.model.Book

data class GenreListUiState(
    val loading: Boolean = false,
    val listByGenre:List<Book> = emptyList(),
    val errorMsg:String? = null
)