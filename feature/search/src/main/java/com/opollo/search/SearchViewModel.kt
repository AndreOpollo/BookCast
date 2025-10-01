package com.opollo.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opollo.domain.model.Book
import com.opollo.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
): ViewModel(){

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Book>> = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .onEach { _isSearching.value = true }
        .flatMapLatest {
            query->
            if(query.isBlank()){
                flowOf(emptyList())
            } else {
                bookRepository.searchBooks(query)
            }
        }
        .onEach { _isSearching.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query:String){
        _searchQuery.value = query
    }
    fun clearSearch(){
        _searchQuery.value = ""
    }
}