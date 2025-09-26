package com.opollo.auth.presentation

sealed class AuthState{
    data object Loading: AuthState()
    data object Authenticated: AuthState()
    data object Unauthenticated: AuthState()
}