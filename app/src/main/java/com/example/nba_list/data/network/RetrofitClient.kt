package com.example.nba_list.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A client for creating Retrofit instances.
 *
 * @param apiKey The API key used for authorization in API requests.
 */
class RetrofitClient(
    private val apiKey: String
){
    private val baseUrl = "https://api.balldontlie.io"


    /**
     * Creates and returns a Retrofit client configured with the base URL and interceptors.
     *
     * @return A configured Retrofit instance.
     */
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