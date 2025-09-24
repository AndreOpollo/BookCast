package com.opollo.details

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter
import com.opollo.player.presentation.PlayerEvent
import com.opollo.player.presentation.PlayerViewModel

@Composable
fun DetailsScreen(book: Book,
                  onBackPressed:()->Unit,
                  viewModel: DetailsViewModel = hiltViewModel(),
                  playerViewModel: PlayerViewModel = hiltViewModel(),
                  onPlayClicked:()->Unit
){
    var isFavorite by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Overview") }
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(book.urlRss) {
        if(book.urlRss.isNotEmpty()){
            viewModel.getBookChapters(book.urlRss)
        }

        Log.d("DetailsScreen","${playerViewModel.uiState.value}")

    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.coverArt)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                                startY = 0f,
                                endY = 400f
                            )
                        )
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        val icon =
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        val tint =
                            if (isFavorite) MaterialTheme.colorScheme.primary else Color.White
                        Icon(icon, contentDescription = "Favorite", tint = tint)
                    }
                }
            }
        }
        item {
            Column(modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = book.authors.joinToString(separator = ", "){"${it.firstName} ${it.lastName}"  },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = "Duration",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatTotalTime(book.totalTime),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    FilledTonalButton(onClick = {}, modifier = Modifier.weight(1f)) {
                            Row {
                                Icon(Icons.Default.Download,
                                    "Download",
                                    modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Download Book")
                            }
                        }
                        Button(onClick = {
                            if(state.chapters.isNotEmpty()){
                                onPlayClicked()
                                playerViewModel.onEvent(
                                    PlayerEvent.LoadBook(book,state.chapters)
                                )
                                playerViewModel.onEvent(PlayerEvent.PlayChapter(0))
                            }
                        }, modifier = Modifier.weight(1f)) {
                            Row {
                                Icon(Icons.Default.PlayArrow,
                                    "Play Book",
                                    modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Play Book")
                            }
                        }
                    }
                }
            }
        item {
            Column{
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    TabButton("Overview",
                        isSelected = selectedTab == "Overview",
                        onClick = {selectedTab = "Overview"})
                    TabButton("Chapters",
                        isSelected = selectedTab == "Chapters",
                        onClick = {selectedTab = "Chapters"})

                }
                HorizontalDivider()

            }
        }
        item {
            AnimatedContent(
                targetState = selectedTab,
                label = "TabContent",
                modifier = Modifier.padding(16.dp)
            ) {
                tab->
                when(tab){
                    "Overview"->{
                        val description = book.description
                        val annotatedString = buildAnnotatedString {
                            append(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT))
                        }
                        Text(
                            text = annotatedString,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
                        )
                    }
                    "Chapters"->{
                        ChapterList(chapters = state.chapters)
                    }
                }
            }

        }
    }
}

@Composable
fun ChapterList(chapters:List<Chapter>){
    Column {
        chapters.forEach{chapter->
            ChapterItem(chapter, onClick = {})
        }
    }

}

@Composable
fun ChapterItem(chapter: Chapter,
                onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = "Chapter ${chapter.chapterNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = chapter.duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

        }

    }

}

@Composable
fun TabButton(text:String,isSelected:Boolean,onClick:()->Unit){
    TextButton(onClick) {
        Text(text,
            color = if(isSelected)MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

private fun formatTotalTime(time:String):String{
    val parts = time.split(":")

    if(parts.size<2) return time

    return when(parts.size){
        3->"${parts[0]} hrs  ${parts[1]} mins ${parts[2]} s"
        2->"${parts[0]} hrs ${parts[1]} mins"
        else -> time
    }

}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview(){

}