package com.example.nba_list.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(
    private val apiKey: String
){
    private val baseUrl = "https://api.balldontlie.io"

    fun createClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .addInterceptor(AuthorizationInterceptor(apiKey))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}