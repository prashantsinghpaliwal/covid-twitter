package com.bigsteptech.covidtracker.data.remote


data class Tweet(
    val tweetData: Data,
    val userData : User,
    val meta: Meta
)

