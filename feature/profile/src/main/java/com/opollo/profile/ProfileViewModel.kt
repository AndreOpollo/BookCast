package com.opollo.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opollo.domain.repository.AuthRepository
import com.opollo.domain.repository.ReadingProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _isAnonymous = MutableStateFlow(false)
    val isAnonymous = _isAnonymous.asStateFlow()

    private val _userEmail = MutableStateFlow<String>("")
    val userEmail =_userEmail.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getAuthState().collectLatest {
                user->
                if(user!=null){
                    _isAnonymous.value = user.isAnonymous
                    _userEmail.value = user.email.toString()
                }
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}