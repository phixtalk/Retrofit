package com.retrofit.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL = "http://10.0.2.2:9000/"//will be converted to http://127.0.0.1:9000/ by emulator at runtime

    //create OKHttp Client
    private val OkHttp = OkHttpClient.Builder()

    //create Retrofit builder, and integrate GSON with retrofit builder so we can convert json to objects
    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttp.build())

    //Create Retrofit Instance
    private val retrofit = builder.build()

    //here we will pass the class which implements the interface we defined,
    //then using the instance of the class we this function returns, we can call the functions
    //that we defined in the interface

    fun <T> builderService(serviceType: Class<T>) : T {
        return retrofit.create(serviceType)
    }

}