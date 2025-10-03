package com.opollo.currents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.domain.model.Author
import com.opollo.domain.model.Book
import com.opollo.domain.model.ReadingProgress
import kotlin.collections.isNotEmpty

@Composable
fun CurrentlyReadingScreen(
    currentlyReading: List<ReadingProgress> = sampleReadingProgress(),
    onBookClick: (Book) -> Unit = {},
    onFavoriteClick: (Book, Boolean) -> Unit = { _, _ -> },
    onPlayClick: (Book) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Currently Reading",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (currentlyReading.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${currentlyReading.size} books",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(currentlyReading) { progress ->
                    CurrentlyReadingCard(
                        progress = progress,
                        onBookClick = { onBookClick(progress.book) },
                        onFavoriteClick = { isFavorite ->
                            onFavoriteClick(progress.book, isFavorite)
                        },
                        onPlayClick = { onPlayClick(progress.book) }
                    )
                }
            }

    }
}

fun sampleReadingProgress(): List<ReadingProgress> {
    return listOf(
        ReadingProgress(
            book = Book(
                id = "1",
                title = "The Great Gatsby",
                description = "A classic American novel",
                language = "English",
                authors = listOf(Author("1", "F. Scott", "Fitzgerald")),
                copyrightYear = "1925",
                numSections = "9",
                totalTime = "4h 32m",
                totalTimeSecs = 16320,
                urlRss = "",
                urlZipFile = "",
                coverArt = "https://example.com/gatsby.jpg"
            ),
            currentChapter = 3,
            totalChapters = 9,
            progressPercentage = 0.35f,
            timeRemaining = "2h 58m left",
            isFavorite = true,
            lastReadTimestamp = 0
        ),
        ReadingProgress(
            book = Book(
                id = "2",
                title = "To Kill a Mockingbird",
                description = "A gripping tale of racial injustice",
                language = "English",
                authors = listOf(Author("2", "Harper", "Lee")),
                copyrightYear = "1960",
                numSections = "31",
                totalTime = "12h 17m",
                totalTimeSecs = 44220,
                urlRss = "",
                urlZipFile = "",
                coverArt = "https://example.com/mockingbird.jpg"
            ),
            currentChapter = 12,
            totalChapters = 31,
            progressPercentage = 0.68f,
            timeRemaining = "3h 56m left",
            isFavorite = false,
            lastReadTimestamp = 0
        )
    )
}
