package com.example.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DummyTweets(
    @SerialName("data")
    val `data`: List<Data>? = null,
    @SerialName("meta")
    val meta: Meta? = null
)