package com.opollo.home

import com.opollo.domain.model.Book

data class HomeUiState(
    val loading:Boolean = false,
    val currentlyReadingBookList:List<CurrentlyReadingBook> = emptyList(),
    val recommendedList:List<Book> = emptyList(),
    val newReleasesList:List<Book> = emptyList(),
    val errorMsg:String? = null
)