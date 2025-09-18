package com.opollo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.opollo.data.local.dao.AuthorDao
import com.opollo.data.local.dao.BookAuthorDao
import com.opollo.data.local.dao.BookDao
import com.opollo.data.local.entities.AuthorEntity
import com.opollo.data.local.entities.BookAuthorCrossRef
import com.opollo.data.local.entities.BookEntity

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class,
        BookAuthorCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun authorDao(): AuthorDao
    abstract fun bookAuthorDao(): BookAuthorDao
}