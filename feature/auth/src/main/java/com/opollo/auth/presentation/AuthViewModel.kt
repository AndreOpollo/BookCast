package com.opollo.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opollo.domain.repository.AuthRepository
import com.opollo.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()


    init {
        viewModelScope.launch {
            authRepository.getAuthState().collectLatest {
                user->
                _authState.value = if(user!=null){
                    AuthState.Authenticated
                }else{
                    AuthState.Unauthenticated
                }
            }
        }
    }

    fun loginUser(email:String,password:String){
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when(val result = authRepository.signInWithEmail(email,password)) {
                is Resource.Error<*> -> {
                    _loginError.value = result.throwable.message?:"Something went wrong"
                }
                Resource.Loading -> {
                    _authState.value = AuthState.Loading
                }
                is Resource.Success<*> -> {}
            }

        }
    }

    fun loginAsGuest(){
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when(val result = authRepository.signInAsGuest()) {
                is Resource.Error<*> -> {
                    _loginError.value = result.throwable.message
                }
                Resource.Loading -> {
                    _authState.value = AuthState.Loading
                }
                is Resource.Success<*> -> {}
            }
        }
    }

    fun register(email: String,password: String){
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when(val result = authRepository.register(email,password)) {
                is Resource.Error<*> -> {
                    _loginError.value = result.throwable.message
                }
                Resource.Loading -> {
                    _authState.value = AuthState.Loading
                }
                is Resource.Success<*> -> {}
            }
        }
    }

    fun upgradeAccount(email:String,password: String){
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when(val result = authRepository.upgradeAccount(email,password)) {
                is Resource.Error<*> -> {
                    _loginError.value = result.throwable.message
                }
                Resource.Loading -> {
                    _authState.value = AuthState.Loading
                }
                is Resource.Success<*> -> {}
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}