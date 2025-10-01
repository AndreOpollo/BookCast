package com.opollo.domain.repository

import com.opollo.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addFavorite(bookId:String): Resource<Unit>
    suspend fun removeFavorite(bookId: String): Resource<Unit>
    suspend fun getFavoriteBookIds(): Flow<Resource<List<String>>>
    suspend fun isFavorite(bookId: String):Flow<Boolean>
}