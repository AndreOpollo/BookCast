package com.opollo.home

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(),
               onBookClicked:(Book)->Unit,
               ){
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("Home Screen:Current","${state.value.currentlyReadingBookList}")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("BookCast",
                    style = MaterialTheme.typography.headlineLarge)},
            )
        },
        content = {paddingValues ->
         Content(modifier = Modifier.padding(paddingValues),
             uiState = state.value,
             onBookClicked = onBookClicked,)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){

}