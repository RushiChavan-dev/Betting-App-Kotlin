package com.example.rushi.common

import com.example.rushi.utils.Resource
import retrofit2.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

abstract class BaseDataSource {
    @Inject
    lateinit var retrofit: Retrofit
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = retrofit.callbackExecutor().run { call() }
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Timber.d(message)
        return Resource.error("Network call has failed for a following reason: $message")
    }


}