package com.opollo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest



data class Book(
    val id:Int,
    val title:String,
    val author:String,
    val imageUrl:String
)

data class CurrentlyReadingBook(
    val book: Book,
    val currentChapter:Int,
    val totalChapters:Int
)

@Composable
fun BookCard(
    book: Book,
    isFavorite:Boolean,
    onCardClick:()->Unit,
    onFavoriteToggle:()->Unit,
    modifier:Modifier = Modifier,
    currentChapter: Int? = null,
    totalChapters: Int? = null
){
    val readingProgress = if(currentChapter!=null && totalChapters!=null && totalChapters>0){
        currentChapter.toFloat()/totalChapters.toFloat()
    }else{
        null
    }
    val chapterProgress = if(currentChapter!=null && totalChapters!=null){
        "Chapter $currentChapter/$totalChapters"
    }else{
        null
    }
    Card(
        modifier = modifier.fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("")
                    .crossfade(true)
                    .build(),
                contentDescription = "${book.title} book cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 80.dp, height = 110.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ){
                Text(book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text("by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)

                Text("1hr 30mins")
                if(readingProgress!=null && currentChapter!=null){
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(progress = {readingProgress})
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = chapterProgress!!, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onFavoriteToggle) {
                Icon(if(isFavorite)Icons.Default.Favorite
                else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Gray)
            }

        }
    }
}