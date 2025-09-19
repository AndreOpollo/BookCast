package com.opollo.bookcast

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.opollo.bookcast.navigation.NavItem
import com.opollo.bookcast.navigation.NavigationGraph
import com.opollo.discover.DiscoverScreen
import com.opollo.favorites.FavoritesScreen
import com.opollo.home.HomeScreen

@Composable
fun MainApp(){
    val homeBackStack = rememberNavBackStack(NavigationGraph.Home)
    val discoverBackStack = rememberNavBackStack(NavigationGraph.Discover)
    val favoritesBackStack = rememberNavBackStack(NavigationGraph.Favorites)
    val profileBackStack = rememberNavBackStack(NavigationGraph.Profile)

    var currentTab by remember{ mutableStateOf<NavKey>(NavigationGraph.Home) }

    val currentBackStack = when(currentTab){
        NavigationGraph.Home->homeBackStack
        NavigationGraph.Discover->discoverBackStack
        NavigationGraph.Favorites->favoritesBackStack
        NavigationGraph.Profile->profileBackStack
        else -> homeBackStack
    }

    val navItems = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Default.Home,
            key = NavigationGraph.Home
        ),
        NavItem(
            label = "Discover",
            icon = Icons.Default.Search,
            key = NavigationGraph.Discover
        ),
        NavItem(
            label = "Favorites",
            icon = Icons.Default.Favorite,
            key = NavigationGraph.Favorites
        ),
        NavItem(
            label = "Profile",
            icon = Icons.Default.Person,
            key = NavigationGraph.Profile
        ),
    )

    Scaffold(
        bottomBar = {
            NavigationBar { 
                navItems.forEach { 
                    item->
                    NavigationBarItem(
                        selected = currentTab == item.key,
                        onClick = {currentTab = item.key},
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = {Text(item.label)},
                    )
                }
            }
            
        }
    ){paddingValues ->
        NavDisplay(
            backStack = currentBackStack,
            onBack = {currentBackStack.removeLastOrNull()},
            entryProvider = entryProvider {
                entry<NavigationGraph.Home>{
                    HomeScreen()
                }
                entry<NavigationGraph.Discover>{
                    DiscoverScreen(onSearchClick = {})
                }
                entry<NavigationGraph.Favorites>{
                    HomeScreen()
                }
                entry<NavigationGraph.Profile>{
                    DiscoverScreen(onSearchClick = {})
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}