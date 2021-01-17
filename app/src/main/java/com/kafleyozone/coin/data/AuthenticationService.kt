package com.kafleyozone.coin.data

import com.kafleyozone.coin.data.models.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface AuthenticationService {
    @GET("auth/login")
    suspend fun login() : Call<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshAuth() : Call<LoginResponse>

    @GET("auth/logout")
    suspend fun logout() : Call<ResponseBody>
}