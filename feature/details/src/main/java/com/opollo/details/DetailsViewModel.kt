package com.opollo.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opollo.domain.model.Chapter
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
class DetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()


    fun getBookChapters(rssUrl:String){
        _uiState.update {
            it.copy(loading = true, errorMsg = null, chapters = emptyList())
        }
        viewModelScope.launch {
            bookRepository.getBookChapters(rssUrl).collectLatest {
                    result->
                when(result){
                    is Resource.Error<*> -> {
                        _uiState.update {
                            it.copy(loading = false, errorMsg = result.throwable.message)
                        }
                    }
                    Resource.Loading -> {
                        _uiState.update {
                            it.copy(loading = true)
                        }
                    }
                    is Resource.Success<*> -> {
                        _uiState.update {
                            it.copy(loading = false, chapters = result.data as List<Chapter>)
                        }
                    }
                }
            }
        }
    }
}