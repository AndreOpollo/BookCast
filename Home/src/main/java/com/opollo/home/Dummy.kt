package com.opollo.home

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
val recommendedBooks = listOf(
    Book(1, "The Alchemist", "Paulo Coelho", "https://picsum.photos/seed/reco1/300/450"),
    Book(2, "To Kill a Mockingbird", "Harper Lee", "https://picsum.photos/seed/reco2/300/450"),
    Book(3, "1984", "George Orwell", "https://picsum.photos/seed/reco3/300/450"),
    Book(4, "The Great Gatsby", "F. Scott Fitzgerald", "https://picsum.photos/seed/reco4/300/450")
)

val currentlyReadingBooks = listOf(
    CurrentlyReadingBook(
        book = Book(5, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "https://picsum.photos/seed/curr1/300/450"),
        currentChapter = 5,
        totalChapters = 20
    ),
    CurrentlyReadingBook(
        book = Book(6, "Atomic Habits", "James Clear", "https://picsum.photos/seed/curr2/300/450"),
        currentChapter = 12,
        totalChapters = 25
    )
)

val newReleases = listOf(
    Book(7, "Project Hail Mary", "Andy Weir", "https://picsum.photos/seed/new1/300/450"),
    Book(8, "Klara and the Sun", "Kazuo Ishiguro", "https://picsum.photos/seed/new2/300/450"),
    Book(9, "The Lincoln Highway", "Amor Towles", "https://picsum.photos/seed/new3/300/450")
)