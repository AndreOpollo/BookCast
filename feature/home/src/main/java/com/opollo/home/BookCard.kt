package com.opollo.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.opollo.domain.model.Book

@Composable
fun BookCard(book: Book,
             modifier: Modifier = Modifier,
             onBookClicked:(Book)->Unit){
    Column(
        modifier = modifier.width(140.dp)
    ){
        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(book.coverArt)
            .crossfade(true)
            .build(),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(3f/4f)
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = {onBookClicked(book)})
            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.authors.firstOrNull()?.let { author ->
                "${author.firstName} ${author.lastName}".trim()
            } ?: "Unknown Author",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CurrentlyReadingBookCard(data: CurrentlyReadingBook){
    val progress = data.currentChapter.toFloat()/data.totalChapters

    Column(modifier = Modifier.width(140.dp)){
        BookCard(book = data.book, onBookClicked = {})
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "${data.currentChapter}/${data.totalChapters} Chapters",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            LinearProgressIndicator(
                progress = {progress},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BookSection(
    modifier: Modifier = Modifier,
    title:String,
    books:List<Book> = emptyList(),
    currentlyReadingBooks:List<Book> = emptyList(),
    onBookClicked: (Book) -> Unit
){
    val hasContent = books.isNotEmpty()
    if(hasContent) {
        Column(modifier){
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(currentlyReadingBooks){
                currentlyReading->
                BookCard(currentlyReading, onBookClicked = onBookClicked)
            }
            items(books) { book ->
                    BookCard(book, onBookClicked = onBookClicked)

            }
        }
    }
        }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview(){
}