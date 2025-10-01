package com.opollo.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opollo.domain.model.Book
import com.opollo.domain.repository.BookRepository
import com.opollo.domain.repository.FavoritesRepository
import com.opollo.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val favoritesRepository: FavoritesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavoriteBooks()

    }


    private fun loadFavoriteBooks(){
        _uiState.update {
            it.copy(isLoading = true, errorMsg = null)
        }
        viewModelScope.launch {
            favoritesRepository.getFavoriteBookIds().collectLatest {
                result->
                when(result) {
                    is Resource.Error<*> -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMsg = result.throwable.message)
                        }
                    }
                    Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success<*> -> {
                        val bookIds = result.data as List<String>
                        if(bookIds.isEmpty()){
                            _uiState.update {
                                it.copy(favoriteBooks = emptyList())
                            }
                        }else{
                            fetchBooksFromIds(bookIds)
                        }
                    }
                }
            }

        }
    }

    private fun fetchBooksFromIds(bookIds:List<String>){
        _uiState.update {
            it.copy(isLoading = true, errorMsg = null)
        }

        viewModelScope.launch {
            val books = mutableListOf<Book>()

            bookIds.forEach {
                bookId->
                bookRepository.getBookById(bookId).collectLatest {
                    result->
                    when(result){
                        is Resource.Error<*> -> {
                            _uiState.update {
                                it.copy(isLoading = false, errorMsg = result.throwable.message)
                            }
                        }
                        Resource.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Success<*> -> {
                            result.data.let { books.add(it as Book) }

                        }
                    }
                }
            }
            _uiState.update {
                it.copy(isLoading = false, favoriteBooks = books)
            }
        }
    }

    fun toggleFavorite(bookId:String,isFavorite:Boolean){
        viewModelScope.launch {
            if(isFavorite){
                favoritesRepository.removeFavorite(bookId)
            }else{
                favoritesRepository.addFavorite(bookId)
            }
        }
    }

    fun isFavorite(bookId:String): Flow<Boolean> {
        return flow {
            favoritesRepository.isFavorite(bookId).collect { isFav ->
                emit(isFav)
            }
        }
    }
}