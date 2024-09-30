package com.example.nba_list.data.repositories

import android.util.Log
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.network.ApiService
import com.example.nba_list.data.network.HttpErrorException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface PlayersRepository{

    val players: StateFlow<List<Player>>
    var cursor: Int

    suspend fun loadPlayers()
}

class PlayersRepositoryImpl(
    private val service: ApiService
): PlayersRepository {

    private val pageSize = 35

    private val _players = MutableStateFlow(emptyList<Player>())
    override val players: StateFlow<List<Player>>
        get() = _players.asStateFlow()

    override var cursor = 0


    override suspend fun loadPlayers() {
        Log.d("loadPlayers", "$cursor")
        delay(1000L)
        service.getPlayers(cursor, pageSize).also{ response ->
            if (response.isSuccessful){
                response.body()?.let { body ->
                    cursor = body.meta.nextCursor
                    _players.update {
                        it + body.data
                    }
                }
            }else{
                throw HttpErrorException(response.code(), response.errorBody()?.toString())
            }
        }
    }
}

