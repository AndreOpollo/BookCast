package com.opollo.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.opollo.domain.repository.AuthRepository
import com.opollo.domain.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository{
    override suspend fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener{
            firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }

    }

    override suspend fun register(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            Resource.Success(result)
        }catch (e: Exception){
            Resource.Error(e)
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(result)

        }catch (e: Exception){
            Resource.Error(e)
        }
    }

    override suspend fun signInAsGuest(): Resource<AuthResult> {
        return try {
            val result = auth.signInAnonymously().await()
            Resource.Success(result)

        }catch(e:Exception){
            Resource.Error(e)
        }

    }

    override suspend fun upgradeAccount(
        email: String,
        password: String
    ): Resource<AuthResult> {
        val currentUser = auth.currentUser
        if(currentUser==null||!currentUser.isAnonymous ){
            return Resource.Error(Error("No Anonymous User is Currently Signed In"))
        }
        return try {
            val credentials = EmailAuthProvider.getCredential(email,password)
            val result = currentUser.linkWithCredential(credentials).await()
            Resource.Success(result)

        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}