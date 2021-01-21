package com.kafleyozone.coin.data

import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.utils.EP_LOGIN
import com.kafleyozone.coin.utils.EP_LOGOUT
import com.kafleyozone.coin.utils.EP_REFRESH
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenticationService {
    @GET(EP_LOGIN)
    suspend fun login(@Header("Authorization") basicToken: String) : LoginResponse

    @GET(EP_REFRESH)
    suspend fun refreshAuth() : LoginResponse

    @GET(EP_LOGOUT)
    suspend fun logout() : ResponseBody
}