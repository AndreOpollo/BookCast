package com.opollo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.opollo.data.local.entities.BookAuthorCrossRef
import com.opollo.data.local.entities.BookWithAuthors

@Dao
interface BookAuthorDao {

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookWithAuthors(bookId:String): BookWithAuthors?

    @Transaction
    @Query("SELECT * FROM books WHERE title = :title")
    suspend fun getBookWithAuthorsByTitle(title:String): BookWithAuthors?

    @Transaction
    @Query("SELECT * FROM books ORDER BY title ASC")
    suspend fun getAllBooksWithAuthors():List<BookWithAuthors>

    @Transaction
    @Query("SELECT * FROM books WHERE genre=:genre ORDER BY title ASC")
    suspend fun getAllBooksWithAuthorsByGenre(genre:String):List<BookWithAuthors>

    @Upsert
    suspend fun insertBookAuthorCrossRef(crossRefs: List<BookAuthorCrossRef>)
}