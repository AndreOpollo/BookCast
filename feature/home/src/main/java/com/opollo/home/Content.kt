package com.opollo.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Content(modifier: Modifier = Modifier){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)

    ){
        item {
            BookSection(
                "Currently Reading",
                items = currentlyReadingBooks
            )
        }
        item {
            BookSection(
                "Recommended For You",
                items = recommendedBooks
            )
        }
        item{
            BookSection(
                title = "New Releases",
                items = newReleases
            )
        }

    }
}