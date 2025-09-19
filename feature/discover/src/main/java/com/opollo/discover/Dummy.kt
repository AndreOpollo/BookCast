package com.opollo.discover

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class Genre(
    val name:String,
    @DrawableRes val image: Int,
    val color: Color
)

val sampleGenres = listOf(
    Genre("Fiction", R.drawable.book_cover, Color(0xFFE53935)),
    Genre("Science Fiction", R.drawable.book_cover, Color(0xFF1E88E5)),
    Genre("Mystery & Thriller", R.drawable.book_cover, Color(0xFF5E35B1)),
    Genre("History", R.drawable.book_cover, Color(0xFF8D6E63)),
    Genre("Children's Fiction", R.drawable.book_cover, Color(0xFFFDD835)),
    Genre("Poetry", R.drawable.book_cover, Color(0xFF00ACC1)),
    Genre("Philosophy", R.drawable.book_cover, Color(0xFF3949AB)),
    Genre("Action & Adventure", R.drawable.book_cover, Color(0xFFFB8C00)),
)