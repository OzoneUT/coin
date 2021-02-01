package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.data.models.User
import com.kafleyozone.coin.data.network.AccountService
import okhttp3.ResponseBody
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val accountService: AccountService,
) {
    suspend fun getAccount(): Resource<User> {
        val response = accountService.getAccount()
        return if (response.isSuccessful) {
            Log.i(AuthRepository.TAG, "got account successfully - getAccount()")
            Resource.success(response.body())
        } else {
            Resource.error("For your security, please login again.", null)
        }
    }

    suspend fun logout(): Resource<ResponseBody> {
        val response = accountService.logout()
        return if (response.isSuccessful) {
            Log.i(AuthRepository.TAG, "logged user out successfully")
            Resource.success(null)
        } else {
            Log.e(AuthRepository.TAG, "got error trying to log out user")
            Resource.error("", null)
        }
    }
}