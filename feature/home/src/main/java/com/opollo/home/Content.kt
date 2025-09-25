package com.opollo.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.domain.model.Book

@Composable
fun Content(modifier: Modifier = Modifier,
            uiState: HomeUiState,
            onBookClicked:(Book)->Unit){

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)

    ){
        item {
            BookSection(
                title = "Continue Reading",
                books = emptyList(),
                onBookClicked = onBookClicked
            )
        }
        item {
            BookSection(
                title = "Recommended For You",
                books = uiState.recommendedList,
                onBookClicked = onBookClicked
            )
        }
        item{
            BookSection(
                title = "New Releases",
                books = uiState.recommendedList,
                onBookClicked = onBookClicked
            )
        }

    }
}