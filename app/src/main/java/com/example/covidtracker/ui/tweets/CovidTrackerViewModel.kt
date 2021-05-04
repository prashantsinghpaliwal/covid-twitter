package com.example.covidtracker.ui.tweets

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.covidtracker.common.Resource
import com.example.covidtracker.data.remote.Tweet
import com.example.covidtracker.network.TwitterService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CovidTrackerViewModel @ViewModelInject constructor(private val twitterService: TwitterService) :
    ViewModel() {

    fun getTweets(query: String): Observable<Resource<MutableList<Tweet>>> {
        return twitterService.getTweetsWithUser(query).toObservable()
            .subscribeOn(Schedulers.io())
            .map(Resource.Companion::success)
            .startWithItem(Resource.loading())
            .onErrorReturn(Resource.Companion::error)
    }

    fun getMoreTweets(query: String, lastTweedId: String): Observable<Resource<MutableList<Tweet>>> {
        return twitterService.getMoreTweetsWithUser(query, lastTweedId).toObservable()
            .subscribeOn(Schedulers.io())
            .map(Resource.Companion::success)
            .startWithItem(Resource.loading())
            .onErrorReturn(Resource.Companion::error)
    }


}