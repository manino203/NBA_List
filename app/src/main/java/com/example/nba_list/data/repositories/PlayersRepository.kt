package com.example.nba_list.data.repositories

import com.example.nba_list.data.models.Player
import com.example.nba_list.data.network.ApiService
import com.example.nba_list.data.network.HttpErrorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for a repository that handles player data.
 */
interface PlayersRepository {

    /**
     * Loads a list of players from the data source.
     *
     * @return A Flow that emits a list of players.
     */
    suspend fun loadPlayers(): Flow<List<Player>>
}

/**
 * Implementation of the [PlayersRepository] interface.
 *
 * @param service The [ApiService] used to fetch player data from remote source.
 */
class PlayersRepositoryImpl(
    private val service: ApiService
): PlayersRepository {

    private val pageSize = 35
    private var cursor = 0
    /**
     * Loads players from the remote API.
     *
     * This method fetches players using the current cursor and page size.
     * If the response is successful, it updates the cursor for the next request
     * and emits the list of players.
     *
     * @return A Flow that emits a list of players.
     * @throws HttpErrorException If the API request fails with an error.
     */
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

