package com.opollo.bookcast

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.opollo.bookcast.navigation.NavItem
import com.opollo.bookcast.navigation.NavigationGraph
import com.opollo.details.DetailsScreen
import com.opollo.discover.DiscoverScreen
import com.opollo.favorites.FavoritesScreen
import com.opollo.genres.GenreListScreen
import com.opollo.home.HomeScreen
import com.opollo.player.FullScreenPlayer
import com.opollo.player.MiniPlayer
import com.opollo.player.presentation.PlayerViewModel
import com.opollo.profile.ProfileSettingsScreen
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: PlayerViewModel = hiltViewModel()){
    val homeBackStack = rememberNavBackStack(NavigationGraph.Home)
    val discoverBackStack = rememberNavBackStack(NavigationGraph.Discover)
    val favoritesBackStack = rememberNavBackStack(NavigationGraph.Favorites)
    val profileBackStack = rememberNavBackStack(NavigationGraph.Profile)

    val state by viewModel.uiState.collectAsState()

    var currentTab by remember{ mutableStateOf<NavKey>(NavigationGraph.Home) }


    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var isPlayerVisible  = state.currentBook != null

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        Log.d("SheetDebug", "State: ${scaffoldState.bottomSheetState.currentValue}")
    }

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

    Scaffold (
        bottomBar = {
            if (scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded && currentBackStack.size == 1) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentTab == item.key,
                            onClick = { currentTab = item.key },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }

        }

    ) { innerPadding ->
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                when (scaffoldState.bottomSheetState.currentValue) {
                    SheetValue.Hidden -> { Spacer(modifier = Modifier.height(1.dp)) }
                    SheetValue.Expanded -> { FullScreenPlayer() }
                    SheetValue.PartiallyExpanded -> { MiniPlayer(onExpand = { scope.launch { scaffoldState.bottomSheetState.expand() } }) } }
            },
            sheetShape = RectangleShape,
            sheetDragHandle = {},
            sheetPeekHeight = if(isPlayerVisible)innerPadding.calculateBottomPadding() +  70.dp else 0.dp,
        ) {
            NavDisplay(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(bottom = if(isPlayerVisible)70.dp else 0.dp),
                backStack = currentBackStack,
                onBack = { currentBackStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<NavigationGraph.Home> {
                        HomeScreen(onBookClicked = { book ->
                            homeBackStack.add(NavigationGraph.Details(book))
                        })
                    }
                    entry<NavigationGraph.Discover> {
                        DiscoverScreen(onSearchClick = {}, onGenreClicked = { genre ->
                            discoverBackStack.add(NavigationGraph.GenreList(genre))
                        })
                    }
                    entry<NavigationGraph.Favorites> {
                        HomeScreen(onBookClicked = {})
                    }
                    entry<NavigationGraph.Profile> {
                        ProfileSettingsScreen()
                    }
                    entry<NavigationGraph.Details> { entry ->
                        DetailsScreen(
                            entry.book,
                            onBackPressed = { currentBackStack.removeLastOrNull() },
                            onPlayClicked = {
                                isPlayerVisible = true
                                scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                            })
                    }
                    entry<NavigationGraph.GenreList> { entry ->
                        GenreListScreen(genre = entry.genre, onBackClick = {
                            currentBackStack.removeLastOrNull()
                        },
                            onBookClick = {book->
                                currentBackStack.add(NavigationGraph.Details(book))
                            },
                            onFavoriteToggle = {})
                    }
                },
            )
        }
    }
}