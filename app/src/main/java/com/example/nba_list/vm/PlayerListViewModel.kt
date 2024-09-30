package com.example.nba_list.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nba_list.R
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.network.HttpErrorException
import com.example.nba_list.data.repositories.PlayersRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


data class PlayerListUiState(
    val loading: Boolean = false,
    val players: List<Player> = emptyList(),
    val error: Int? = null
)

class PlayerListViewModel(
    private val playersRepo: PlayersRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PlayerListUiState())
    val uiState: StateFlow<PlayerListUiState>
        get() = _uiState.asStateFlow()

    fun loadPlayers(){

        _uiState.update{
            it.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try{
                playersRepo.loadPlayers()
            }catch (e: Exception){
                _uiState.update{
                    it.copy(error = handleError(e))
                }
            }
            _uiState.update{
                it.copy(loading = false)
            }
        }
    }

    fun collectChanges(){
        viewModelScope.launch(Dispatchers.Main) {
            playersRepo.players.collectLatest{
                _uiState.update{ state ->
                    state.copy(players = it)
                }
            }
        }
    }

    fun clearError(){
        _uiState.update {
            it.copy(error = null)
        }
    }

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