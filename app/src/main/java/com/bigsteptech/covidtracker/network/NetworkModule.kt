package live.performlive.network

import android.content.Context
import android.net.ConnectivityManager
import com.bigsteptech.covidtracker.BuildConfig
import com.bigsteptech.covidtracker.CovidApp
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.UnknownHostException


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        httpClient.addInterceptor(logging)

        val skipLoggingInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder().build()
            val header = request.header("logging")
            header?.let {
                val loggingEnabled = it.toBoolean()
                logging.setLevel(if (loggingEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            }
            chain.proceed(request)
        }

        val interceptor = Interceptor { chain ->
            val requestWithSessionId = chain.request().newBuilder()
            if (chain.request().header("Authorization") == null) {
                requestWithSessionId.addHeader("Authorization", "Bearer " + "AAAAAAAAAAAAAAAAAAAAAPZ1PAEAAAAAcsePjLWtw" +
                        "xiMU1Nz7inP4qLwRxQ%3DKzLWADWMoFNvAO2silPGH5xUVHYkJjHYamp7zgdjGlaMoE780M")
            }
            chain.proceed(requestWithSessionId.build())
        }

        httpClient.addInterceptor(skipLoggingInterceptor)

        val networkConnectionIntercepter = Interceptor { chain ->

            val cm = CovidApp.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork?.isConnectedOrConnecting == true

            if (!isConnected) {
                throw UnknownHostException()
            }

            chain.proceed(chain.request())
        }

        httpClient.addInterceptor(networkConnectionIntercepter)
        httpClient.addInterceptor(interceptor)

        return httpClient.build()
    }

    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    }

    @Provides
    fun provideRetrofit(builder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return builder
                .baseUrl(BuildConfig.BASE_ENDPOINT)
                .client(okHttpClient)
                .build()
    }


}