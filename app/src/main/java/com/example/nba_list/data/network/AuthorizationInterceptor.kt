package com.example.nba_list.data.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor for adding an authorization header to HTTP requests.
 *
 * @param apiKey The API key to be included in the authorization header.
 */
class AuthorizationInterceptor(
    private val apiKey: String
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", apiKey)
                .build()
        )
    }
}