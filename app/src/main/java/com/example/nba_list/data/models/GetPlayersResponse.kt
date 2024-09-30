package com.example.nba_list.data.models

import com.google.gson.annotations.SerializedName

data class GetPlayersResponse (
    val data: List<Player>,
    val meta: MetaData
)

data class MetaData(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    val perPage: Int,
)