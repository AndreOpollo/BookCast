package com.opollo.domain.repository

import com.google.firebase.auth.AuthResult
import com.opollo.domain.util.Resource

interface AuthRepository {
    suspend fun register(email:String,password:String): Resource<AuthResult>
    suspend fun signInWithEmail(email:String,password:String):Resource<AuthResult>
    suspend fun signInAsGuest():Resource<AuthResult>
    suspend fun upgradeAccount(email:String,password:String):Resource<AuthResult>
    suspend fun logout()
}