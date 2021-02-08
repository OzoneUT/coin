package com.kafleyozone.coin.data.network

import com.kafleyozone.coin.data.network.models.LoginResponse
import com.kafleyozone.coin.data.network.models.RegistrationRequest
import com.kafleyozone.coin.data.network.models.TokenResponse
import com.kafleyozone.coin.utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {
    @GET(EP_LOGIN)
    suspend fun login(@Header(HEADER_AUTHORIZATION) basicToken: String): Response<LoginResponse>

    @POST(EP_REGISTER)
    suspend fun register(@Body request: RegistrationRequest) : Response<ResponseBody>

    @GET(EP_REFRESH)
    fun refreshAuth(
        @Header(HEADER_REFRESH) value: String = "true",
        @Header(HEADER_AUTHORIZATION) refreshToken: String
    ): Call<TokenResponse>
}