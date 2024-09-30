package com.example.nba_list.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TopBarUiState(
    val loading: Boolean = false,
    val title: String = ""
)

class TopBarViewModel(

): ViewModel() {

    val uiState = mutableStateOf(TopBarUiState())

    fun setTitle(newTitle: String){
        uiState.value = uiState.value.copy(title = newTitle)
    }

    fun setLoading(loading: Boolean){
        uiState.value = uiState.value.copy(loading = loading)
    }

}