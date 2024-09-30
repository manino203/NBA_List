package com.example.nba_list.data.network

import com.example.nba_list.data.models.GetPlayersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface representing the API service for fetching data.
 */
interface ApiService {

    /**
     * Retrieves a list of players from the API.
     *
     * This method makes a network request to get players, using pagination to
     * control the number of players returned.
     *
     * @param cursor The pagination cursor indicating the position to start
     * loading players from. This value is used for fetching the next set of players.
     * @param pageSize The number of players to retrieve in a single request.
     * It determines how many player records will be returned.
     *
     * @return A [Response] object containing a [GetPlayersResponse]. The
     * response will be successful if the request is executed correctly and
     * contains the requested data.
     */
    @GET("v1/players")
    suspend fun getPlayers(
        @Query("cursor") cursor: Int,
        @Query("per_page") pageSize: Int
    ): Response<GetPlayersResponse>
}