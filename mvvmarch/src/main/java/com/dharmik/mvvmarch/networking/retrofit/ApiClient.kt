package com.dharmik.mvvmarch.networking.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val OKHTTP_TIMEOUT = 30 // seconds
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitHeader: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    private const val DEBUG = true
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/" /// Latest url

    /**
     * @return [Retrofit] object its single-tone
     */
    fun getApiClient(): Retrofit {
        if (!::retrofit.isInitialized) {
            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()

            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOKHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofit
    }

    /**
     * settings like caching, Request Timeout, Logging can be configured here.
     *
     * @return [OkHttpClient]
     */
    private fun getOKHttpClient(): OkHttpClient {
        return if (!::okHttpClient.isInitialized) {
            val builder = OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)

            if (DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            builder.addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("Accept", "application/json")
                chain.proceed(requestBuilder.build())
            }
            okHttpClient = builder.build()
            okHttpClient
        } else {
            okHttpClient
        }
    }


    fun getHeaderClient(header: String): Retrofit {

        if (::retrofitHeader.isInitialized)
            return retrofitHeader

        val builder = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor(header))
                .retryOnConnectionFailure(true)
                .connectTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(OKHTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)

        if (DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        val okHttpClient = builder.build()

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

        retrofitHeader = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofitHeader
    }

    private class HeaderInterceptor internal constructor(private val headerString: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("token", headerString)
                    .build()

            return chain.proceed(request)
        }
    }
}