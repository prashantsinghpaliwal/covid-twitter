package com.example.covidtracker.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("data")
    val `data`: User
)

@Serializable
data class User(
    @SerialName("name")
    val name: String = "",

    @SerialName("id")
    val id: String = "",

    @SerialName("username")
    val userName: String = "",

    @SerialName("profile_image_url")
    val profileImageUrl: String = ""
)