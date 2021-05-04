package com.example.covidtracker

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

@HiltAndroidApp
class CovidApp : Application(){

    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        RxJavaPlugins.setErrorHandler { e: Throwable ->
            if (e is UndeliverableException) {
                Log.w("RxJavaPlugins", e.cause)
            }
            if (e is IOException || e is SocketException) {
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                return@setErrorHandler
            }
        }

    }
}