package com.bigsteptech.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hashtag(
    @SerialName("end")
    val end: Int? = null,
    @SerialName("start")
    val start: Int? = null,
    @SerialName("tag")
    val tag: String? = null
)