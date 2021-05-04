package com.example.covidtracker.network

import com.example.covidtracker.data.remote.DummyTweets
import com.example.covidtracker.data.remote.Meta
import com.example.covidtracker.data.remote.Tweet
import com.example.covidtracker.data.remote.UserData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import live.performlive.network.ApiErrorConverter
import live.performlive.network.RetrofitExceptionFactory
import javax.inject.Inject

interface TwitterService {

    fun getUserDetails(userId: String): Single<UserData>

    fun getTweetsWithUser(searchQuery: String): Single<MutableList<Tweet>>

    fun getMoreTweetsWithUser(searchQuery: String, lastTweedId: String): Single<MutableList<Tweet>>

    class Impl @Inject constructor(
        private val twitterApi: TwitterApi,
        private val apiErrorConverter: ApiErrorConverter
    ) : TwitterService {

        override fun getUserDetails(userId: String): Single<UserData> {
            return twitterApi.getUserDetails(userId)
                .compose(RetrofitExceptionFactory.single(apiErrorConverter))
                .subscribeOn(Schedulers.io())
        }

        override fun getMoreTweetsWithUser(
            searchQuery: String,
            lastTweedId: String
        ): Single<MutableList<Tweet>> {

            var meta = Meta()

            return twitterApi.getMoreTweets(searchQuery, lastTweedId)
                .toObservable()
                .flatMapIterable {
                    meta = it?.meta!!
                    it?.data
                }.flatMapSingle {
                    val data = it
                    twitterApi.getUserDetails(data.authorId!!)
                        .map {
                            Tweet(data, it.data, meta)
                        }
                        .subscribeOn(Schedulers.io())
                }.toList()
                .compose(RetrofitExceptionFactory.single(apiErrorConverter))
                .subscribeOn(Schedulers.io())
        }

        override fun getTweetsWithUser(
            searchQuery: String
        ): Single<MutableList<Tweet>> {

            var meta = Meta()

            return twitterApi.getTweets(searchQuery)
                .toObservable()
                .flatMapIterable {
                    meta = it?.meta!!
                    it?.data
                }.flatMapSingle {
                    val data = it
                    twitterApi.getUserDetails(data.authorId!!)
                        .map {
                            Tweet(data, it.data, meta)
                        }
                        .subscribeOn(Schedulers.io())
                }.toList()
                .compose(RetrofitExceptionFactory.single(apiErrorConverter))
                .subscribeOn(Schedulers.io())
        }

    }
}