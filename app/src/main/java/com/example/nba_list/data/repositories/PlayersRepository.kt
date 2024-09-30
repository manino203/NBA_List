package com.example.nba_list.data.repositories

import com.example.nba_list.data.models.Player
import com.example.nba_list.data.network.ApiService
import com.example.nba_list.data.network.HttpErrorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

interface PlayersRepository {
    suspend fun loadPlayers(): Flow<List<Player>>
}

class PlayersRepositoryImpl(
    private val service: ApiService
): PlayersRepository {

    private val pageSize = 35
    private var cursor = 0

    override suspend fun loadPlayers(): Flow<List<Player>> = flow {
        val response = service.getPlayers(cursor, pageSize)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                cursor = body.meta.nextCursor
                emit(body.data)
            }
        } else {
            throw HttpErrorException(response.code(), response.errorBody()?.toString())
        }
    }
}

