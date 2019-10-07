package com.example.achar.di

import com.example.achar.BuildConfig
import com.example.achar.di.models.HttpInterceptor
import com.example.achar.webService.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val MODULE_NAME = "Network Module"
val networkModule = Module(MODULE_NAME, false) {
    bind<HttpLoggingInterceptor>() with singleton { getHttpLoggingInterceptor() }
    bind<OkHttpClient>() with singleton { getOkHttpClient(instance()) }
    bind<Retrofit>() with singleton { getRetrofit(instance()) }
    bind<ApiService>() with singleton { getApiService(instance()) }
}


private fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    httpBuilder.addInterceptor(HttpInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .interceptors().apply {
//            if (BuildConfig.DEBUG) {
                this.add(httpLoggingInterceptor)
//            }
        }
    return httpBuilder.build()
}

private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}

private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gsonBuilder = GsonBuilder()
        .serializeNulls().create()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(gsonBuilder)
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient).build()
}


private fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

