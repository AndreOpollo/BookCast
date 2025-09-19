package com.opollo.data.mappers

import android.R.attr.description
import com.opollo.data.local.entities.AuthorEntity
import com.opollo.data.local.entities.BookEntity
import com.opollo.data.local.entities.BookWithAuthors
import com.opollo.data.remote.model.AuthorDto
import com.opollo.data.remote.model.BookDto
import com.opollo.domain.model.Author
import com.opollo.domain.model.Book


fun BookWithAuthors.toDomain(): Book {
    return Book(
        id = book.id,
        title = book.title,
        description = book.description,
        language = book.language,
        authors = authors.map { it.toDomain() },
        copyrightYear = book.copyrightYear,
        numSections = book.numSections,
        totalTime = book.totalTime,
        totalTimeSecs = book.totalTimeSecs,
        urlRss =book.urlRss,
        urlZipFile = book.urlZipFile,
        coverArt = book.coverArt
    )
}

fun AuthorEntity.toDomain(): Author{
    return Author(
        id = id,
        firstName = firstName,
        lastName = lastName
    )
}

fun BookDto.toEntity(genre:String?): BookEntity{
    return BookEntity(
        id,
        title,
        description,
        copyrightYear,
        language,
        numSections,
        totalTime,
        totalTimeSecs,
        urlRss,
        urlZipFile,
        genre = genre,
        coverArt
    )
}

fun AuthorDto.toEntity(): AuthorEntity{
    return AuthorEntity(
        id,
        firstName,
        lastName
    )
}
