package com.opollo.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.opollo.data.local.entities.AuthorEntity

@Dao
interface AuthorDao {
    @Upsert
    suspend fun insertAuthor(authors:List<AuthorEntity>)
}