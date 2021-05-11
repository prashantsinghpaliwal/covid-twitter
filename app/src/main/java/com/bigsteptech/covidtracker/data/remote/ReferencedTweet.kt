package com.bigsteptech.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReferencedTweet(
    @SerialName("id")
    val id: String? = null,
    @SerialName("type")
    val type: String? = null
)