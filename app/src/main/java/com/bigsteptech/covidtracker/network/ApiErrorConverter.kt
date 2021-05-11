package live.performlive.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException

interface ApiErrorConverter {

    @Throws(IOException::class)
    fun <T> convert(body: ResponseBody?, clazz: Class<T>): T?

    class Impl internal constructor(private val mRetrofit: Retrofit) : ApiErrorConverter {

        @Throws(IOException::class)
        override fun <T> convert(body: ResponseBody?, clazz: Class<T>): T? {
            val errorConverter: Converter<ResponseBody, T> = mRetrofit.responseBodyConverter(clazz, arrayOfNulls(0))
            return if (body != null) errorConverter.convert(body) else null
        }
    }

}