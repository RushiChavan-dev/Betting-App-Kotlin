package com.example.rushi.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.rushi.data.locale.CacheHelper
import com.example.rushi.data.remote.ApiService
import com.example.rushi.BuildConfig
import com.example.rushi.services.RemoteConfigService
import com.example.rushi.utils.AnalyticsHelper
import com.example.rushi.utils.NetworkUtils
import com.example.rushi.utils.OddUtilHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun provideHttpCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Singleton
    @Provides
    fun provideOkHttp(cache: Cache, @ApplicationContext appContext: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.readTimeout(15000, TimeUnit.MILLISECONDS)
        client.writeTimeout(70000, TimeUnit.MILLISECONDS)
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        client.cache(cache)
        client.addInterceptor(interceptor)
        client.addInterceptor { chain ->
            val request = chain.request()

            var response: Response? = null
            var tryCount = 1
            while (tryCount <= 3) {
                try {
                    response = chain.proceed(request)
                    break
                } catch (e: Exception) {
                    if (!NetworkUtils.isNetworkAvailable(appContext)) {
                        // if no internet, no need to retry
                        throw e
                    }
                    if ("Canceled".equals(e.message, ignoreCase = true)) {
                        // Request canceled, do not retry
                        throw e
                    }
                    if (tryCount >= 3) {
                        // max retry count reached, giving up
                        throw e
                    }

                    try {
                        // sleep delay * try count (e.g. 1st retry after 3000ms, 2nd after 6000ms, etc.)
                        Thread.sleep(3000 * tryCount.toLong())
                    } catch (e1: InterruptedException) {
                        throw RuntimeException(e1)
                    }
                    tryCount++
                }
            }
            response!!
        }
        client.addInterceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            original.url.newBuilder().build()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return client.build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideCacheHelper(@ApplicationContext appContext: Context) = CacheHelper(appContext)

    @Singleton
    @Provides
    fun provideRemoteConfig(): RemoteConfigService = RemoteConfigService.getInstance()

    @Singleton
    @Provides
    fun provideAnalyticsHelper(): AnalyticsHelper = AnalyticsHelper.getInstance()

    @Singleton
    @Provides
    fun provideOddUtilHelper(): OddUtilHelper = OddUtilHelper.getInstance()

}