package com.retrofit.services

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

//So this is the summary of Retrofit implementation (Retrofit implements a Builder design pattern)
//We create a singleton class ServiceBuilder that is responsible for implementing the Retrofit builder class
/**
 * The Retrofit builder class has the following methods
 * Retrofit.Builder() //instantiates the builder class
 *  .baseUrl(URL) //accepts the remote url
 *  .addConverterFactory() //accepts the GsonConverterFactory.create() class necessary for converting json into java/kotlin objects
 *  .client() // accepts the OkHttpClient.Builder() object
 *  .build() //creates the retrofit instance
 *  .create() //accepts our predefined Interface instance that contains the method for getting data from the endpoint webservice
* */

object ServiceBuilder {

    private const val URL = "http://10.0.2.2:9000/"//will be converted to http://127.0.0.1:9000/ by emulator at runtime

    //Create custom Interceptor to apply Headers Application wide
    //create an anonymous inner class and implement the interceptor interface
    var headerInterceptor = object: Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            //using the chain method, we can get the reference to the http request
            var request = chain.request()
            //next we can modify our request and add some header to it
            request = request.newBuilder()
                .addHeader("x-device-type", Build.DEVICE)
                .addHeader("x-device-type", Locale.getDefault().language)
                .build()

            //finally use the chain.proceed method to resume the request pipeline, so that the headers can be sent to the server
            val response = chain.proceed(request)
            return response
        }

    }


    //create OKHttp Client
    //By default all the retrofit timeouts is 10secs, so if a network call takes more than 10secs
    // automatically the onfailure method will be executed
    //we can modify this by using the callTimeout method
    private val OkHttp = OkHttpClient.Builder()
        .callTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(headerInterceptor)

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