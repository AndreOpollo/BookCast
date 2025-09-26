package com.opollo.bookcast

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.auth.presentation.AuthState
import com.opollo.auth.presentation.AuthViewModel

@Composable
fun MainApp(authViewModel: AuthViewModel = hiltViewModel()){
    val authState by authViewModel.authState.collectAsState()

    when(authState) {
        AuthState.Authenticated -> {
            AppNavigation()
        }
        AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center){
                Text("Please wait...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
        AuthState.Unauthenticated -> {
            AuthNavigation()
        }
    }
}