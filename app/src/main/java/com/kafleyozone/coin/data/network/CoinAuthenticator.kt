package com.kafleyozone.coin.data.network

import android.util.Log
import com.kafleyozone.coin.data.TokenRepository
import com.kafleyozone.coin.utils.HEADER_AUTHORIZATION
import com.kafleyozone.coin.utils.isRefreshingToken
import com.kafleyozone.coin.utils.toBearerToken
import com.kafleyozone.coin.utils.usingBasicAuth
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class CoinAuthenticator @Inject constructor(
    private val authenticationServiceLazy: Lazy<AuthenticationService>,
    private val tokenRepository: TokenRepository
) : Authenticator {

    companion object {
        const val TAG = "CoinAuthenticator"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.i(TAG, "In CoinAuthenticator")
        val authService = authenticationServiceLazy.get() ?: return null

        // if the causing request was made with basic auth, we don't need to refresh anything.
        if (usingBasicAuth(response.request)) {
            Log.i(TAG, "no need to refresh b/c basic auth was used")
            return null
        }

        // if the 401-causing request was because of a refresh attempt, don't try again.
        if (isRefreshingToken(response.request)) {
            Log.i(TAG, "no need to refresh b/c previous refresh attempt failed")
            return null
        }

        // get the refresh token on this thread
        val refreshToken = runBlocking {
            return@runBlocking toBearerToken(tokenRepository.getCachedRefreshToken())
        }

        // make the refresh request with Refresh-Token header and refresh token
        // if that request is successful, update the tokenRepo w/ new values
        val refreshResponse = authService.refreshAuth(refreshToken = refreshToken).execute()
        if (refreshResponse.isSuccessful) {
            val tokens = refreshResponse.body()
            if (tokens == null) {
                Log.e(TAG, "refresh response was successful but tokens were null")
                return null
            }
            Log.i(TAG, "saving the new tokens to dataStore")
            runBlocking {
                tokenRepository.cacheNewAuth(tokens.accessToken, tokens.refreshToken)
            }

            // return the original request with new access token
            Log.i(TAG, "returning the old request with new access token")
            return response.request.newBuilder()
                .removeHeader(HEADER_AUTHORIZATION)
                .addHeader(HEADER_AUTHORIZATION, toBearerToken(tokens.accessToken))
                .build()

        } else {
            Log.i(TAG, "refreshAuth call was not successful")
            return null
        }
    }
}