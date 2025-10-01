package com.opollo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.opollo.domain.repository.FavoritesRepository
import com.opollo.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): FavoritesRepository {

    private fun getUserFavoritesRef() = firestore.collection("users")
        .document(auth.currentUser?.uid?:throw IllegalStateException("User not authenticated"))
        .collection("favorites")

    override suspend fun addFavorite(bookId: String): Resource<Unit> {
        return withContext(Dispatchers.IO){
            try {
                if(auth.currentUser?.uid == null){
                    return@withContext Resource.Error(Exception("User not authenticated"))
                }

                getUserFavoritesRef()
                    .document(bookId)
                    .set(mapOf(
                        "bookId" to bookId,
                        "timeStamp" to System.currentTimeMillis()
                    )).await()

                Resource.Success(Unit)
            }catch (e: Exception){
                Resource.Error(e)
            }
        }
    }

    override suspend fun removeFavorite(bookId: String): Resource<Unit> {
        return withContext(Dispatchers.IO){
            try {
                if(auth.currentUser?.uid == null){
                    return@withContext Resource.Error(Exception("User not authenticated"))
                }

                getUserFavoritesRef()
                    .document(bookId)
                    .delete()
                    .await()

                Resource.Success(Unit)

            }catch (e:Exception){
                Resource.Error(e)
            }
        }
    }

    override suspend fun getFavoriteBookIds(): Flow<Resource<List<String>>> {
        return withContext(Dispatchers.IO){
            callbackFlow {
                try {
                    if(auth.currentUser?.uid == null){
                        trySend(Resource.Error(Exception("User is not authenticated")))
                        close()
                        return@callbackFlow
                    }
                    trySend(Resource.Loading)

                    val listener = getUserFavoritesRef()
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .addSnapshotListener {
                            snapshot,error->
                            if(error!=null){
                                trySend(Resource.Error(error))
                                return@addSnapshotListener
                            }
                            val bookIds = snapshot?.documents?.mapNotNull {
                                it.getString("bookId")
                            } ?: emptyList()
                            trySend(Resource.Success(bookIds))
                        }
                    awaitClose {
                        listener.remove()
                    }

                }catch (e: Exception){
                    trySend(Resource.Error(e))
                    close()
                }
            }
        }
    }

    override suspend fun isFavorite(bookId: String): Flow<Boolean> {
        return withContext(Dispatchers.IO){
            callbackFlow {
                try {
                    if(auth.currentUser?.uid == null){
                        trySend(false)
                        close()
                        return@callbackFlow
                    }
                    val listener = getUserFavoritesRef()
                        .document(bookId)
                        .addSnapshotListener {
                            snapshot,error ->
                            if(error!=null){
                                trySend(false)
                                return@addSnapshotListener

                            }
                            trySend(snapshot?.exists() == true)
                        }
                    awaitClose {
                        listener.remove()
                    }
                }catch (e: Exception){
                    trySend(false)
                    close()

                }
            }
        }
    }
}