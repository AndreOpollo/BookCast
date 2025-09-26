package com.opollo.bookcast

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.opollo.auth.presentation.AuthViewModel
import com.opollo.auth.presentation.login.LoginScreen
import com.opollo.auth.presentation.register.RegisterScreen
import com.opollo.bookcast.navigation.NavigationGraph

@Composable
fun AuthNavigation(){
    val authBackStack = rememberNavBackStack(NavigationGraph.Login)

    NavDisplay(
        backStack = authBackStack,
        onBack = {authBackStack.removeLastOrNull()},
        entryProvider = entryProvider {
            entry<NavigationGraph.Login>{
                LoginScreen(
                    onNavigateToRegister = {authBackStack.add(NavigationGraph.Register)},
                )
            }
            entry<NavigationGraph.Register>{
                RegisterScreen(
                    onNavigateToLogin = {authBackStack.removeLastOrNull()},
                )
            }
        }
    )
}