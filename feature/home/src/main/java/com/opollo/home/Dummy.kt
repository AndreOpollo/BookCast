package com.opollo.home

import com.opollo.domain.model.Book

//data class Book(
//    val id:Int,
//    val title:String,
//    val author:String,
//    val imageUrl:String
//)

data class CurrentlyReadingBook(
    val book: Book,
    val currentChapter:Int,
    val totalChapters:Int
)


