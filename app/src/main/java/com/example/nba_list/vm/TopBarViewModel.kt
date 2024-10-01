package com.example.nba_list.vm

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


/**
 * Data class representing the UI state for the top bar.
 *
 * @param loading Indicates whether to display progress bar.
 * @param title The title displayed in the top bar.
 */
@Immutable
data class TopBarUiState(
    val loading: Boolean = false,
    val title: String = ""
)

/**
 * ViewModel for managing the top bar's UI state.
 */
class TopBarViewModel: ViewModel() {

    val uiState = mutableStateOf(TopBarUiState())
    /**
     * Sets a new title for the top bar.
     *
     * @param newTitle The new title to set.
     */
    fun setTitle(newTitle: String){
        uiState.value = uiState.value.copy(title = newTitle)
    }
    /**
     * Sets the loading state for the top bar.
     *
     * @param loading Indicates whether the top bar should display a loading state.
     */
    fun setLoading(loading: Boolean){
        uiState.value = uiState.value.copy(loading = loading)
    }

}