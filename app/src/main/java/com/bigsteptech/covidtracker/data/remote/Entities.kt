package com.bigsteptech.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entities(
    @SerialName("hashtags")
    val hashtags: List<Hashtag>? = null,
    @SerialName("mentions")
    val mentions: List<Mention>? = null,
    @SerialName("urls")
    val urls: List<Url>? = null
)