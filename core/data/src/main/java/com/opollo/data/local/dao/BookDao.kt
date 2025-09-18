package com.opollo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.opollo.data.local.entities.BookEntity

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY title ASC")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE genre=:genre ORDER BY title ASC")
    suspend fun getBooksByGenre(genre:String):List<BookEntity>

    @Query("SELECT * FROM books WHERE id=:bookId")
    suspend fun getBookById(bookId:String): BookEntity

    @Query("SELECT DISTINCT genre FROM books WHERE genre IS NOT NULL ")
    suspend fun getDistinctGenres():List<String?>

    @Upsert
    suspend fun insertBooks(books: List<BookEntity>)


}