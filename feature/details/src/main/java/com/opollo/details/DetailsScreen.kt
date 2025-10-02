package com.opollo.details

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsScreen(book: Book,
                  onBackPressed:()->Unit,
                  viewModel: DetailsViewModel = hiltViewModel(),
                  playerViewModel: PlayerViewModel,
){

    var selectedTab by remember { mutableStateOf("Overview") }
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val isFavorite by viewModel.isFavorite(book.id).collectAsState(initial = false)



    LaunchedEffect(book.urlRss) {
        if(book.urlRss.isNotEmpty()){
            viewModel.getBookChapters(book.urlRss)
        }

    }
    LaunchedEffect(Unit){
        Log.d("Format",formatChapterDuration("00:10:40"))
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
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
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = { viewModel.toggleFavorite(book.id,isFavorite) }) {
                        val icon =
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        val tint =
                            if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        Icon(icon, contentDescription = "Favorite", tint = tint)
                    }
                }

        }
        item {
            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.coverArt)
                            .placeholder(R.drawable.placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Book Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(200.dp)
                            .aspectRatio(3f / 4f)
                            .clip(MaterialTheme.shapes.medium)

                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = book.authors.joinToString(separator = ", ") { "${it.firstName} ${it.lastName}" }
                            .trim(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = "Duration",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatTotalTime(book.totalTime),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    FilledTonalButton(onClick = {}, modifier = Modifier.weight(1f)) {
                            Row {
                                Icon(Icons.Default.Download,
                                    "Download",
                                    modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Download",
                                    style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        Button(onClick = {

                            if(state.chapters.isNotEmpty()){
                                playerViewModel.onEvent(
                                    PlayerEvent.LoadBook(book,state.chapters),
                                    context
                                )
                            }
                        }, modifier = Modifier.weight(1f),
                            enabled = state.chapters.isNotEmpty()) {

                                Icon(Icons.Default.PlayArrow,
                                    "Play Book",
                                    modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text(if(state.chapters.isEmpty()) "Loading..." else "Play",
                                    style = MaterialTheme.typography.labelLarge)

                        }
                    }
                }
            }
        item {
            Column{
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    TabButton("Overview",
                        isSelected = selectedTab == "Overview",
                        onClick = {selectedTab = "Overview"})
                    TabButton("Chapters",
                        isSelected = selectedTab == "Chapters",
                        onClick = {selectedTab = "Chapters"})

                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))

            }
        }
        item {
            AnimatedContent(
                targetState = selectedTab,
                label = "TabContent",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
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
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
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
        chapters.forEachIndexed{index,chapter->
            ChapterItem(chapter, onClick = {})

            if(index<chapters.size-1){
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f)
                )
            }
        }
    }

}

@Composable
fun ChapterItem(chapter: Chapter,
                onClick: () -> Unit){
    LaunchedEffect(Unit) {
        Log.d("Chapter Duration", chapter.duration)
        Log.d("Formated Chapter Duration",formatChapterDuration(chapter.duration))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chapter.chapterNumber.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatChapterDuration(chapter.duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = "Play Chapter",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TabButton(text:String,isSelected:Boolean,onClick:()->Unit){
    TextButton(onClick,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
        Text(text,
            style = MaterialTheme.typography.titleLarge,
            color = if(isSelected)MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

private fun formatTotalTime(time:String):String{
    val parts = time.trim().split(":")

    if(parts.size<2) return time

    return when(parts.size){
        3->{
            val hours = parts[0].toIntOrNull() ?: 0
            if(hours>0) "${parts[0]}h ${parts[1]}m"
            else "${parts[1]}m"
        }
        2->"${parts[0]}h ${parts[1]}m"
        else -> time
    }

}

private fun formatChapterDuration(duration:String):String{

    val parts = duration.trim().split(":")

    if(parts.size<2) return duration

    return when(parts.size){
        3->{
            val hours = parts[0].toIntOrNull() ?: 0
            val mins = parts[1].toIntOrNull() ?: 0
            if(hours > 0) "${parts[0]}h ${parts[1]}m ${parts[2]}s"
            else if(mins>0)"${parts[1]}m ${parts[2]}s"
            else "${parts[2]}s"
        }
        2->{
            val mins = parts[0].toIntOrNull() ?: 0
            if(mins > 0) "${parts[0].trim()}m ${parts[1].trim()}s"
            else "${parts[2]}s"
        }
        else -> duration
    }

}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview(){

}