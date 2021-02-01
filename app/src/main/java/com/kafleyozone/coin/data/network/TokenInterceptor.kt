package com.kafleyozone.coin.data.network

import android.util.Log
import com.kafleyozone.coin.data.TokenRepository
import com.kafleyozone.coin.utils.HEADER_AUTHORIZATION
import com.kafleyozone.coin.utils.hasAuthHeader
import com.kafleyozone.coin.utils.toBearerToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    companion object {
        const val TAG = "TokenInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.i(TAG, "Call intercepted by TokenInterceptor")
        val originalRequest = chain.request()

        // We don't need to change the Authorization header if one already exists.
        if (hasAuthHeader(originalRequest)) {
            Log.i(TAG, "Intercepted request already has an auth header. No changes made.")
            return chain.proceed(originalRequest)
        }

        // get the access token by running the suspend function on this thread
        val accessToken: String = runBlocking {
            return@runBlocking toBearerToken(tokenRepository.getCachedAccessToken())
        }

        // add the authorization header to the old request and proceed using it as the new
        Log.i(TAG, "Adding an authorization header with an access token.")
        val authorizedRequest = chain.request().newBuilder()
            .header(HEADER_AUTHORIZATION, accessToken)
            .build()
        return chain.proceed(authorizedRequest)
    }
}