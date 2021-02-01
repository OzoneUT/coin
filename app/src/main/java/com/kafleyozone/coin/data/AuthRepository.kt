package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.RegistrationRequest
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.data.network.AuthenticationService
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) {
    companion object {
        const val TAG = "AuthRepository"
        const val EMAIL_TAKEN = "email_taken"
    }

    suspend fun login(basicValue: String): Resource<LoginResponse> {
        val response = authenticationService.login(basicValue)
        val tokens = response.body()?.tokens
        val user = response.body()?.user?.email
        return if (response.isSuccessful && tokens != null && user != null) {
            tokenRepository.cacheNewAuth(tokens.accessToken, tokens.refreshToken, email = user)
            Resource.success(response.body())
        } else {
            Resource.error(
                "We couldn't log you in. Check your username and password.", null)
        }
    }

    suspend fun register(request: RegistrationRequest): Resource<String> {
        val response = authenticationService.register(request)
        return if (response.isSuccessful) {
            Log.i(TAG, "Registration successful")
            Resource.success("success")
        } else {
            val message = "We couldn't register you. Please try again."
            var reason: String? = null
            if (response.code() == HttpURLConnection.HTTP_NOT_ACCEPTABLE) {
                reason = EMAIL_TAKEN
            }
            Resource.error(message, reason)
        }
    }

    suspend fun getLocalUser(): String {
        return tokenRepository.getCachedUserEmail()
    }
}