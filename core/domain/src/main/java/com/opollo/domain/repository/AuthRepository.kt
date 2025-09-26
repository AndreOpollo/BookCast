package com.opollo.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.opollo.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getAuthState(): Flow<FirebaseUser?>
    suspend fun register(email:String,password:String): Resource<AuthResult>
    suspend fun signInWithEmail(email:String,password: String):Resource<AuthResult>
    suspend fun signInAsGuest():Resource<AuthResult>
    suspend fun upgradeAccount(email:String,password: String):Resource<AuthResult>
    suspend fun logout()
}