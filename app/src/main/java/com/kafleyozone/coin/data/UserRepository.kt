package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.RegistrationRequest
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.data.models.User
import com.kafleyozone.coin.data.network.AccountService
import com.kafleyozone.coin.data.network.AuthenticationService
import okhttp3.ResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val accountService: AccountService,
    private val tokenRepository: TokenRepository
) {
    companion object {
        const val TAG = "UserRepository"
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

    suspend fun getAccount(): Resource<User> {
        val response = accountService.getAccount()
        return if (response.isSuccessful) {
            Log.i(TAG, "got account successfully - getAccount()")
            Resource.success(response.body())
        } else {
            Resource.error("For your security, please login again.", null)
        }
    }

    suspend fun logout(): Resource<ResponseBody> {
        val response = accountService.logout()
        return if (response.isSuccessful) {
            Log.i(TAG, "logged user out successfully")
            Resource.success(null)
        } else {
            Log.e(TAG, "got error trying to log out user")
            Resource.error("", null)
        }
    }

    suspend fun getLocalUser(): String {
        return tokenRepository.getCachedUserEmail()
    }
}