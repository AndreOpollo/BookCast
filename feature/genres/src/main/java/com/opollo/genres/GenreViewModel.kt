package com.opollo.genres

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.opollo.domain.model.Book
import com.opollo.domain.repository.BookRepository
import com.opollo.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(GenreListUiState())
    val uiState = _uiState.asStateFlow()

    fun getBooksByGenre(genre:String){
        _uiState.update {
            it.copy(loading = true, errorMsg = null)
        }
        viewModelScope.launch{
            bookRepository.getBooksByGenre(genre).collectLatest {
                result->
                Log.d("GenreViewModel", "Starting to collect books…")
                when(result){
                    is Resource.Error<*> -> {
                        Log.d("GenreViewModel Error", "$result")
                        _uiState.update {
                            it.copy(loading = false, errorMsg = result.throwable.message)
                        }
                    }
                    Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                loading = true
                            )
                        }
                    }
                    is Resource.Success<*> -> {
                        Log.d("GenreViewModel Succes", "$result")

                        _uiState.update {
                            it.copy(
                                loading = false, listByGenre = result.data as List<Book>
                            )
                        }
                    }
                }
            }

        }
    }
}