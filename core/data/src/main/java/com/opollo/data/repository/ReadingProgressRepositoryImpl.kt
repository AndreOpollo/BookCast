package com.opollo.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.opollo.domain.model.ReadingProgress
import com.opollo.domain.repository.ReadingProgressRepository
import com.opollo.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReadingProgressRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ReadingProgressRepository {

    companion object{
        private const val USERS_COLLECTION = "users"
        private const val PROGRESS_SUBCOLLECTION = "reading_progress"
    }
    private var activeListeners = mutableListOf<ListenerRegistration>()

    private val userId:String?
        get() = auth.currentUser?.uid


    override suspend fun listenToReadingProgress(bookId: String): Flow<Resource<ReadingProgress>> =
        withContext(Dispatchers.IO){
            callbackFlow {
                val currentUserId = userId
                if(currentUserId == null) {
                    trySend(Resource.Error(Error("User not authenticated")))
                    close()
                    return@callbackFlow
                }

                val docRef = firestore.collection(USERS_COLLECTION).document(currentUserId)
                    .collection(PROGRESS_SUBCOLLECTION).document(bookId)

                val listenerRegistration = docRef.addSnapshotListener {snapshot, error ->
                    if(error!=null){
                        trySend(Resource.Error(error))
                        return@addSnapshotListener
                    }
                    if(snapshot!=null && snapshot.exists()){
                        val progress = snapshot.toObject(ReadingProgress::class.java)
                        if(progress!=null){
                            trySend(Resource.Success(progress))
                        } else{
                            trySend(Resource.Error(Exception("Failed to parse progress data")))
                        }
                    }else{
                        trySend(Resource.Error(Error("No progress data found on this book")))
                    }
                }
                awaitClose {
                    listenerRegistration.remove()
                }
            }
        }

    override suspend fun updateReadingProgress(progress: ReadingProgress): Resource<Unit> = withContext(
        Dispatchers.IO){
        return@withContext try {
            val currentUserId = userId?: return@withContext Resource.Error(Exception("User is not authenticated"))
            val docRef = firestore.collection(USERS_COLLECTION).document(currentUserId)
                .collection(PROGRESS_SUBCOLLECTION).document(progress.book.id)

            docRef.set(progress).await()

            Resource.Success(Unit)
        }catch (e: Exception){
            Resource.Error(e)
        }
    }
    override suspend fun listenToAllReadingProgress(): Flow<Resource<List<ReadingProgress>>> {
        return callbackFlow {
            var listenerRegistration: ListenerRegistration? = null

            val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val newUserId = firebaseAuth.currentUser?.uid
                Log.d("AuthListener", "Auth changed: $newUserId")

                // Remove old listener
                listenerRegistration?.remove()
                listenerRegistration = null

                if (newUserId == null) {
                    trySend(Resource.Success(emptyList()))
                } else {
                    val colRef = firestore.collection(USERS_COLLECTION)
                        .document(newUserId)
                        .collection(PROGRESS_SUBCOLLECTION)

                    listenerRegistration = colRef.addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(Resource.Error(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val progresses = snapshot.toObjects(ReadingProgress::class.java)
                            trySend(Resource.Success(progresses))
                        } else {
                            trySend(Resource.Success(emptyList()))
                        }
                    }
                }
            }

            auth.addAuthStateListener(authListener)

            awaitClose {
                listenerRegistration?.remove()
                auth.removeAuthStateListener(authListener)
            }
        }
    }

    override fun clearCache() {
        activeListeners.forEach { it.remove() }
        activeListeners.clear()
    }

}