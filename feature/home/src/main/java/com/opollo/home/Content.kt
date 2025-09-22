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
import com.opollo.domain.model.Book

@Composable
fun Content(modifier: Modifier = Modifier,
            uiState: HomeUiState,
            onBookClicked:(Book)->Unit){
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
                items = emptyList(),
                onBookClicked = onBookClicked
            )
        }
        item {
            BookSection(
                "Recommended For You",
                items = uiState.recommendedList,
                onBookClicked = onBookClicked
            )
        }
        item{
            BookSection(
                title = "New Releases",
                items = uiState.recommendedList,
                onBookClicked = onBookClicked
            )
        }

    }
}