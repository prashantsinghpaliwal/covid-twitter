package com.example.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Url(
    @SerialName("description")
    val description: String? = null,
    @SerialName("display_url")
    val displayUrl: String? = null,
    @SerialName("end")
    val end: Int? = null,
    @SerialName("expanded_url")
    val expandedUrl: String? = null,
    @SerialName("images")
    val images: List<Image>? = null,
    @SerialName("start")
    val start: Int? = null,
    @SerialName("status")
    val status: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("unwound_url")
    val unwoundUrl: String? = null,
    @SerialName("url")
    val url: String? = null
)