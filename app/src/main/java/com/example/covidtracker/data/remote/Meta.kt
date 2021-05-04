package com.example.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("newest_id")
    val newestId: String? = null,
    @SerialName("next_token")
    val nextToken: String? = null,
    @SerialName("oldest_id")
    val oldestId: String? = null,
    @SerialName("result_count")
    val resultCount: Int? = null
)