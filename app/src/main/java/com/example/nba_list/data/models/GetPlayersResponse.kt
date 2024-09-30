package com.example.nba_list.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the players API.
 *
 * @param data A list of [Player] objects representing the players.
 * @param meta A [MetaData] object containing metadata related to the response.
 */

data class GetPlayersResponse (
    val data: List<Player>,
    val meta: MetaData
)

/**
 * Data class representing metadata information from the API response.
 *
 * @param nextCursor The cursor for fetching the next page of players.
 * @param perPage The number of players returned per page in the response.
 */
data class MetaData(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    val perPage: Int,
)