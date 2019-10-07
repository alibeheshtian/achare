package com.example.achar.di.models


import com.example.achar.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


@Suppress("UNCHECKED_CAST")
class HttpInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        builder.header("Content-Type", "application/json")
        builder.header("Accept", "application/json")

        builder.header(
            "Authorization",
            Credentials.basic(BuildConfig.TEST_USERNAME, BuildConfig.TEST_PASSWORD)
        )


        request = builder.build() //overwrite old request

        return chain.proceed(request)
    }


}
