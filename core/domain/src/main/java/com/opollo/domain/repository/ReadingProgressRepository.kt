package com.opollo.domain.repository

import com.opollo.domain.model.ReadingProgress
import com.opollo.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReadingProgressRepository {
    suspend fun listenToReadingProgress(bookId:String): Flow<Resource<ReadingProgress>>
    suspend fun updateReadingProgress(progress: ReadingProgress): Resource<Unit>
    suspend fun listenToAllReadingProgress(): Flow<Resource<List<ReadingProgress>>>
    fun clearCache()
}