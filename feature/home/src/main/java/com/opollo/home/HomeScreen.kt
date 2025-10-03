package com.opollo.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
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
             onBookClicked = onBookClicked,
             viewModel)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){

}