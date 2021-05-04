package com.example.covidtracker.common

import io.reactivex.rxjava3.functions.BiFunction
import java.util.*

enum class Status(val value: Int) {
    SUCCESS(1 shl 1), ERROR(1 shl 2), LOADING(1)
}

data class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable): Resource<T> {
            return Resource(Status.ERROR, null, throwable)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T1, T2, R> biResource(combiner: BiFunction<T1, T2, R>): BiFunction<Resource<T1>, Resource<T2>, Resource<R>> {
            return BiFunction { resourceT1, resourceT2 ->
                val state: Int = resourceT1.status.value or resourceT2.status.value
                if (state and Status.ERROR.value == Status.ERROR.value) {
                    val resources = listOf(resourceT1, resourceT2)
                    return@BiFunction error(
                        resources.first().throwable ?: RuntimeException("Runtime Exception")
                    )
                } else if (state and Status.LOADING.value == Status.LOADING.value) {
                    return@BiFunction loading()
                } else if (state == Status.SUCCESS.value) {
                    val combined = combiner.apply(resourceT1.data, resourceT2.data)
                    return@BiFunction success(combined)
                } else {
                    throw IllegalStateException("Unexpected resource state=$state")
                }
            }
        }
    }

}

fun <T> Resource<T>.render(
    onLoading: () -> Unit,
    onSuccess: (data: T?) -> Unit,
    onError: (Throwable?) -> Unit
) {
    when (status) {
        Status.LOADING -> onLoading.invoke()
        Status.ERROR -> onError.invoke(throwable)
        Status.SUCCESS -> onSuccess.invoke(data)
    }
}

fun <T> Resource<T>.successOrError(onSuccess: (data: T?) -> Unit, onError: (Throwable?) -> Unit) {
    when (status) {
        Status.ERROR -> onError.invoke(throwable)
        Status.SUCCESS -> onSuccess.invoke(data)
        else -> Unit
    }
}