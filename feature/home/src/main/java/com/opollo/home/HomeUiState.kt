package com.opollo.home

import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter
import com.opollo.domain.model.ReadingProgress

data class HomeUiState(
    val loading:Boolean = false,
    val currentlyReadingBookList:List<ReadingProgress> = emptyList(),
    val recommendedList:List<Book> = emptyList(),
    val newReleasesList:List<Book> = emptyList(),
    val errorMsg:String? = null,
    val chapters:List<Chapter> = emptyList()
)