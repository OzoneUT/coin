package com.kafleyozone.coin.data.network

import com.kafleyozone.coin.data.domain.SetupAmountEntity
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.utils.EP_ACCOUNT
import com.kafleyozone.coin.utils.EP_ACCOUNT_SETUP
import com.kafleyozone.coin.utils.EP_LOGOUT
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountService {
    @GET(EP_ACCOUNT)
    suspend fun getAccount(): Response<User>

    @POST(EP_ACCOUNT_SETUP)
    suspend fun setupAccount(@Body setupAmountEntities: List<SetupAmountEntity>):
            Response<User>

    @GET(EP_LOGOUT)
    suspend fun logout(): Response<ResponseBody>
}