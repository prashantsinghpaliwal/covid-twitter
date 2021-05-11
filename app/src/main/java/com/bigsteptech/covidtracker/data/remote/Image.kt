package com.bigsteptech.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("height")
    val height: Int? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("width")
    val width: Int? = null
)