package com.example.loftmoney

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoftApp : Application() {
    private lateinit var mApi: Api
    override fun onCreate() {
        super.onCreate()
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://loftschool.com/android-api/basic/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        mApi = retrofit.create(Api::class.java)
    }

    val api: Api
        get() = mApi

}