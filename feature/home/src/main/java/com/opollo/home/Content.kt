package com.opollo.home



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.domain.model.Book

@Composable
fun Content(modifier: Modifier = Modifier,
            uiState: HomeUiState,
            onBookClicked:(Book)->Unit,
            viewModel: HomeViewModel){

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)

    ){
        item {
            BookSection(
                title = "Continue Reading",
                currentlyReadingBooks = uiState.currentlyReadingBookList,
                onBookClicked = onBookClicked,
                viewModel = viewModel )
        }
        item {
            BookSection(
                title = "Recommended For You",
                books = uiState.recommendedList,
                onBookClicked = onBookClicked,
                viewModel = viewModel)
        }
        item{
            BookSection(
                title = "New Releases",
                books = uiState.recommendedList.reversed(),
                onBookClicked = onBookClicked,
                viewModel = viewModel)
        }
    }
}