package com.opollo.domain.repository

import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter
import com.opollo.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(fetchFromRemote:Boolean = false): Flow<Resource<List<Book>>>
    suspend fun getBooksByGenre(genre:String,
                                fetchFromRemote: Boolean = false):Flow<Resource<List<Book>>>
    suspend fun getBookById(bookId:String,
                            fetchFromRemote: Boolean = false): Flow<Resource<Book?>>
    suspend fun getBookByTitle(title:String,
                               fetchFromRemote: Boolean = false):Flow<Resource<Book?>>
    suspend fun getBookChapters(rssUrl:String):Flow<Resource<List<Chapter>>>

    fun searchBooks(query:String):Flow<List<Book>>
}