package com.opollo.bookcast.navigation

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationGraph: NavKey{
    @Serializable
    data object Home: NavigationGraph()
    @Serializable
    data object Discover: NavigationGraph()
    @Serializable
    data object Favorites: NavigationGraph()
    @Serializable
    data object Profile: NavigationGraph()
}

data class NavItem(
    val label:String,
    val icon: ImageVector,
    val key: NavKey
)