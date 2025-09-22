package com.opollo.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(),
               onBookClicked:(Book)->Unit){
    val state = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Hello, Andre")},
                navigationIcon = {
                    Box(modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray))
                },
                actions = {

                }
            )
        },
        content = {paddingValues ->
         Content(modifier = Modifier.padding(paddingValues),
             uiState = state.value,
             onBookClicked = onBookClicked)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(onBookClicked = {})
}