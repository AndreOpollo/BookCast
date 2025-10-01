package com.opollo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.opollo.data.local.entities.BookAuthorCrossRef
import com.opollo.data.local.entities.BookWithAuthors
import kotlinx.coroutines.flow.Flow

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

    @Query("""
    SELECT DISTINCT books.* FROM books
    LEFT JOIN book_author_cross_ref ON books.id = book_author_cross_ref.bookId
    LEFT JOIN authors ON book_author_cross_ref.authorId = authors.id
    WHERE books.title LIKE '%' || :query || '%'
    OR authors.firstName LIKE '%' || :query || '%'
    OR authors.lastName LIKE '%' || :query || '%'
    OR (authors.firstName || ' ' || authors.lastName) LIKE '%' || :query || '%'
    ORDER BY books.title ASC
""")
    fun searchBooks(query: String): Flow<List<BookWithAuthors>>
}