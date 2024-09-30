package com.example.nba_list.data.network

import com.example.nba_list.data.models.GetPlayersResponse
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.models.Team
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v1/players")
    suspend fun getPlayers(
        @Query("cursor") cursor: Int,
        @Query("per_page") pageSize: Int
    ): Response<GetPlayersResponse>
}