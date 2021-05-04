package com.example.covidtracker.di


import com.example.covidtracker.BuildConfig
import com.example.covidtracker.network.TwitterApi
import com.example.covidtracker.network.TwitterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import live.performlive.network.ApiErrorConverter
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
object TwitterModule {


    @Provides
    fun twitterApi(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient): TwitterApi {
        return retrofit
            .baseUrl(BuildConfig.BASE_ENDPOINT)
            .client(okHttpClient)
            .build()
            .create(TwitterApi::class.java)
    }

    @Provides
    fun twitterService(
        api: TwitterApi,
        apiErrorConverter: ApiErrorConverter
    ): TwitterService {
        return TwitterService.Impl(
            api, apiErrorConverter
        )
    }

    @Provides
    fun provide(retrofit: Retrofit) : ApiErrorConverter {
        return ApiErrorConverter.Impl(retrofit)
    }

}