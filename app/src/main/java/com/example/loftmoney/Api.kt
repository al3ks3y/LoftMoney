package com.example.loftmoney

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("auth")
    fun auth(@Query("social_user_id") userId: String): Call<Status>

    @GET("items")
    fun getItems(
        @Query("type") type: String,
        @Query("auth-token") token: String
    ): Call<List<Item?>?>

    @POST("items/add")
    fun addItem(
        @Body request: AddItemRequest,
        @Query("auth-token") token: String
    ): Call<Status>

    @POST("items/remove")
    fun removeItem(
        @Query("id") id: String?,
        @Query("auth-token") token: String?
    ): Call<Status?>?

    @GET("balance")
    fun getBalance(@Query("auth-token") token: String?): Call<BalanceResponce>
}
