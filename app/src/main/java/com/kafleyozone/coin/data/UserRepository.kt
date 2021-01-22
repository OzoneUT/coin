package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.Resource
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    companion object {
        const val TAG = "UserRepository"
    }
    suspend fun login(basicValue: String) : Resource<LoginResponse> {
        try {
            val result =  Resource.success(authenticationService.login(basicValue).body())
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Login failed, stacktrace:")
            e.printStackTrace()
        }
        return Resource.error("Error logging in user", null)
    }
}