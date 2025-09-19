package com.opollo.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Content(modifier: Modifier = Modifier,uiState: HomeUiState){
    LaunchedEffect(Unit) {
        Log.d("Recommended",uiState.recommendedList.toString())
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)

    ){
        item {
            BookSection(
                "Currently Reading",
                items = emptyList()
            )
        }
        item {
            BookSection(
                "Recommended For You",
                items = uiState.recommendedList
            )
        }
        item{
            BookSection(
                title = "New Releases",
                items = uiState.recommendedList
            )
        }

    }
}