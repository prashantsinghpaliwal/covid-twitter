package live.performlive.network

import com.example.covidtracker.network.ErrorsRo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*


data class Error(val title: String?, val fields: Map<String, String>)

open class AppException(val error: Error = Error("App Exception", Collections.emptyMap())) : RuntimeException() {}

object RetrofitExceptionFactory {

    fun <T> single(apiErrorConverter: ApiErrorConverter): SingleTransformer<T, T> {
        return SingleTransformer { upstream: Single<T> -> upstream.onErrorResumeNext { throwable: Throwable? ->
                if (throwable is SocketTimeoutException || throwable is UnknownHostException || throwable is SocketException) {
                    return@onErrorResumeNext Single.error(NoConnectionException(throwable))
                } else if (throwable is HttpException) {
                    when (throwable.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            val message = getApiErrorMessage(apiErrorConverter, throwable)
                            return@onErrorResumeNext Single.error(UnauthorizedException(message))
                        }
                        HttpURLConnection.HTTP_BAD_REQUEST -> {
                            val message = getApiErrorMessage(apiErrorConverter, throwable)
                            return@onErrorResumeNext Single.error(BadRequestException(message))
                        }
                        HttpURLConnection.HTTP_NOT_FOUND -> {
                            val message = getApiErrorMessage(apiErrorConverter, throwable)
                            return@onErrorResumeNext Single.error(NotFoundException(message))
                        }
                    }
                }
                Single.error(throwable)
            }
        }
    }



    private fun getApiErrorMessage(apiErrorConverter: ApiErrorConverter,
                                   httpException: HttpException): Error {
        return try {
            val response = httpException.response()
            val errorsRo = apiErrorConverter.convert(response!!.errorBody(), ErrorsRo::class.java)
            val errors = errorsRo?.errors

            val fields = errors?.map {
                it.field to it.error.map { value -> value.value }.toTypedArray()[0]
            }?.toMap() ?: emptyMap()

            Error(errorsRo?.message, fields)
        } catch (ioe: IOException) {
            Error("", emptyMap())
        }
    }

}