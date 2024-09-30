package com.example.nba_list.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nba_list.R
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.network.HttpErrorException
import com.example.nba_list.data.repositories.PlayersRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


/**
 * Data class representing the UI state for the player list screen.
 *
 * @param loading Indicates whether data is currently being loaded.
 * @param players A list of [Player] objects representing the players.
 * @param error An optional resource ID representing an error message.
 */
data class PlayerListUiState(
    val loading: Boolean = false,
    val players: List<Player> = emptyList(),
    val error: Int? = null
)

/**
 * ViewModel for managing player data in the Player List screen.
 *
 * This ViewModel handles loading player data from the repository,
 * managing loading states, and providing the UI with player data.
 *
 * @param playersRepo The repository responsible for fetching player data.
 */
class PlayerListViewModel(
    private val playersRepo: PlayersRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PlayerListUiState())
    val uiState: StateFlow<PlayerListUiState>
        get() = _uiState.asStateFlow()

    /**
     * Loads the list of players from the repository.
     *
     * This function updates the loading state and collects player data from the repository,
     * updating the UI state with the loaded players or any encountered errors.
     */
    fun loadPlayers(){

        _uiState.update{
            it.copy(loading = true)
        }
        viewModelScope.launch {
            try{
                playersRepo.loadPlayers().collect{ players->
                    _uiState.update {
                        it.copy(players = it.players + players)
                    }
                }
            }catch (e: Exception){
                _uiState.update{
                    it.copy(error = handleError(e))
                }
            }finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    /**
     * Clears any error messages in the UI state.
     */
    fun clearError(){
        _uiState.update {
            it.copy(error = null)
        }
    }

    /**
     * Handles errors that occur during data loading.
     *
     * @param e The exception that occurred.
     * @return A resource ID representing the error message.
     */
    private fun handleError(e: Exception): Int {
        return when (e){
            is HttpErrorException ->
                when (e.code){
                    400 -> R.string.bad_request_error
                    401 -> R.string.not_authorized_error
                    404 -> R.string.resource_not_found_error
                    500 -> R.string.server_error
                    502 -> R.string.too_many_requests_error
                    else -> R.string.generic_error
                }
            is IOException -> R.string.internet_error
            is JsonParseException -> R.string.unexpected_response_error
            is CancellationException -> R.string.request_cancelled_error
            else -> R.string.generic_error
        }
    }

}