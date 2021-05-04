package com.example.covidtracker.network

import com.example.covidtracker.data.remote.DummyTweets
import com.example.covidtracker.data.remote.UserData
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterApi {

    @GET("tweets/search/recent")
    fun getTweets(
        @Query("query") query: String,
        @Query("tweet.fields") userFields: String = "author_id,created_at"
    ): Single<DummyTweets>

    @GET("tweets/search/recent")
    fun getMoreTweets(
        @Query("query") query: String,
        @Query("until_id") lastTweedId: String,
        @Query("tweet.fields") userFields: String = "author_id,created_at"
    ): Single<DummyTweets>

    @GET("users/{userId}")
    fun getUserDetails(
        @Path("userId") userId: String,
        @Query("user.fields") userFields: String = "profile_image_url"
    ): Single<UserData>

}