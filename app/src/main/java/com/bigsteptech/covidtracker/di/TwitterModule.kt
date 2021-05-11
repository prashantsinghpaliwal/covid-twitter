package com.bigsteptech.covidtracker.di


import com.bigsteptech.covidtracker.BuildConfig
import com.bigsteptech.covidtracker.network.TwitterApi
import com.bigsteptech.covidtracker.network.TwitterService
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