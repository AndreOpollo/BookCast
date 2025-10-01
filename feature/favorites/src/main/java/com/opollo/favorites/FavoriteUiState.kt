package com.opollo.favorites

import com.opollo.domain.model.Book

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favoriteBooks: List<Book> = emptyList(),
    val errorMsg: String? = null
)