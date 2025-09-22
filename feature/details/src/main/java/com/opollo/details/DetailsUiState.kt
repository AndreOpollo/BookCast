package com.opollo.details

import com.opollo.domain.model.Chapter

data class DetailsUiState(
    val loading: Boolean = false,
    val chapters:List<Chapter> = emptyList(),
    val errorMsg: String? = null
)