package com.example.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mention(
    @SerialName("end")
    val end: Int? = null,
    @SerialName("start")
    val start: Int? = null,
    @SerialName("username")
    val username: String? = null
)