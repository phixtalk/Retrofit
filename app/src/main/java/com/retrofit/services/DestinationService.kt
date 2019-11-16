package com.retrofit.services

import com.retrofit.models.Destination
import retrofit2.Call
import retrofit2.http.*

interface DestinationService {

    //for multiple query parameters, we use query map
    @Headers("x-device-type: Android", "x-foo: bar")
    @GET("destination")
    fun getDestinationList(@QueryMap filter: HashMap<String, String>): Call<List<Destination>>

    //at runtime, the /{id} with is also present in the path id will be replaced with the id: Int
    @GET("destination/{id}")
    fun getDestination(@Path("id") id: Int): Call<Destination>

    @POST("destination")
    fun addDestination(@Body newDestination: Destination): Call<Destination>

    @FormUrlEncoded
    @PUT("destination/{id}")
    fun updateDestination(
        @Path("id") id: Int,
        @Field("city") city: String,
        @Field("description") description: String,
        @Field("country") country: String
    ) : Call<Destination>

    @DELETE("destination/{id}")
    fun deleteDestination(@Path("id") id: Int): Call<Unit>
}