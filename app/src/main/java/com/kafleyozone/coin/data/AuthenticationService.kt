package com.kafleyozone.coin.data

import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.RegistrationRequest
import com.kafleyozone.coin.utils.EP_LOGIN
import com.kafleyozone.coin.utils.EP_LOGOUT
import com.kafleyozone.coin.utils.EP_REFRESH
import com.kafleyozone.coin.utils.EP_REGISTER
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {
    @GET(EP_LOGIN)
    suspend fun login(@Header("Authorization") basicToken: String) : Response<LoginResponse>

    @POST(EP_REGISTER)
    suspend fun register(@Body request: RegistrationRequest) : Response<ResponseBody>

    @GET(EP_REFRESH)
    suspend fun refreshAuth() : LoginResponse

    @GET(EP_LOGOUT)
    suspend fun logout() : ResponseBody
}