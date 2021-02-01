package com.kafleyozone.coin.data.network

import com.kafleyozone.coin.data.models.User
import com.kafleyozone.coin.utils.EP_ACCOUNT
import com.kafleyozone.coin.utils.EP_LOGOUT
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface AccountService {
    @GET(EP_ACCOUNT)
    suspend fun getAccount(): Response<User>

    @GET(EP_LOGOUT)
    suspend fun logout(): Response<ResponseBody>
}