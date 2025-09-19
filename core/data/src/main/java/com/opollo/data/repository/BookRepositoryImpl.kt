package com.opollo.data.repository


import com.opollo.data.di.JsonRetrofit
import com.opollo.data.local.dao.AuthorDao
import com.opollo.data.local.dao.BookAuthorDao
import com.opollo.data.local.dao.BookDao
import com.opollo.data.local.entities.AuthorEntity
import com.opollo.data.local.entities.BookAuthorCrossRef
import com.opollo.data.local.entities.BookEntity
import com.opollo.data.mappers.toDomain
import com.opollo.data.mappers.toEntity
import com.opollo.data.remote.api.BooksApiService
import com.opollo.data.remote.model.ApiResponse
import com.opollo.data.remote.model.AuthorDto
import com.opollo.domain.model.Book
import com.opollo.domain.repository.BookRepository
import com.opollo.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    @JsonRetrofit private val apiService: BooksApiService,
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val bookAuthorDao: BookAuthorDao
): BookRepository {
    override suspend fun getBooks(fetchFromRemote: Boolean): Flow<Resource<List<Book>>> =
        withContext(Dispatchers.IO) {
            flow {
                emit(Resource.Loading)
                try {
                    val localBooks = bookAuthorDao.getAllBooksWithAuthors()
                    if(localBooks.isNotEmpty()){
                        emit(Resource.Success(localBooks.map { it.toDomain() }))
                    }
                    if(fetchFromRemote || localBooks.isEmpty()){
                        val response = apiService.getBooks()
                        saveApiResponse(response,null)
                        val updatedBooks = bookAuthorDao.getAllBooksWithAuthors()
                        emit(Resource.Success(updatedBooks.map{it.toDomain()}))
                    }


                }catch (e: Exception){
                    emit(Resource.Error(e))

                }

            }
        }

    override suspend fun getBooksByGenre(
        genre: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Book>>>  = withContext(Dispatchers.IO) {
        flow {
            emit(Resource.Loading)
            try {
                val localBooks = bookAuthorDao.getAllBooksWithAuthorsByGenre(genre)
                if(localBooks.isNotEmpty()){
                    emit(Resource.Success(localBooks.map { it.toDomain() }))
                }
                if(fetchFromRemote || localBooks.isEmpty()){
                    val response = apiService.getBooksByGenre(genre)
                    saveApiResponse(response,genre)
                    val updatedBooks = bookAuthorDao.getAllBooksWithAuthorsByGenre(genre)
                    emit(Resource.Success(updatedBooks.map{it.toDomain()}))
                }


            }catch (e: Exception){
                emit(Resource.Error(e))

            }

        }
    }

    override suspend fun getBookById(bookId: String,fetchFromRemote: Boolean): Flow<Resource<Book?>> =
        withContext(Dispatchers.IO) {
        flow {
            emit(Resource.Loading)
            try {
                val book = bookAuthorDao.getBookWithAuthors(bookId)
                if (book!=null) {
                    emit(Resource.Success(book.toDomain()))
                }
                if (fetchFromRemote || book == null) {
                    val response = apiService.getBookById(bookId)
                    saveApiResponse(response,null)
                    val updatedBook = bookAuthorDao.getBookWithAuthors(bookId)
                    emit(Resource.Success(updatedBook?.toDomain()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun getBookByTitle(title: String,fetchFromRemote: Boolean): Flow<Resource<Book?>> =         withContext(Dispatchers.IO) {
        flow {
            emit(Resource.Loading)
            try {
                val book = bookAuthorDao.getBookWithAuthorsByTitle(title)
                if (book!=null) {
                    emit(Resource.Success(book.toDomain()))
                }
                if (fetchFromRemote || book == null) {
                    val response = apiService.getBookByTitle(title)
                    saveApiResponse(response,null)
                    val updatedBook = bookAuthorDao.getBookWithAuthorsByTitle(title)
                    emit(Resource.Success(updatedBook?.toDomain()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }


    private suspend fun saveApiResponse(apiResponse: ApiResponse,genre: String?){
        val bookEntities = mutableListOf<BookEntity>()
        val authorEntities = mutableListOf<AuthorEntity>()
        val crossRefs = mutableListOf<BookAuthorCrossRef>()

        apiResponse.books.forEach {
            book->
            bookEntities.add(book.toEntity(genre))
            book.authors.forEach {
                author->
                authorEntities.add(author.toEntity())
                crossRefs.add(BookAuthorCrossRef(book.id,author.id))
            }
        }
        authorDao.insertAuthor(authorEntities.distinctBy { it.id })
        bookDao.insertBooks(bookEntities)
        bookAuthorDao.insertBookAuthorCrossRef(crossRefs)
    }
}