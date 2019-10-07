package com.example.achar.webService

import com.example.achar.webService.models.sucsess.AddressModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("karfarmas/address")
    fun addresses(): Single<Response<List<AddressModel>>>

    @POST("karfarmas/address")
    fun addressAdd(@Body parameter: Map<String, String>): Single<Response<AddressModel>>
}