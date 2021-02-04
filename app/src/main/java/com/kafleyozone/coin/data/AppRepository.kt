package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.BankInstitutionEntity
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.data.models.User
import com.kafleyozone.coin.data.network.AccountService
import okhttp3.ResponseBody
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val accountService: AccountService,
) {

    companion object {
        private const val TAG = "AppRepository"
    }

    suspend fun getAccount(): Resource<User> {
        val response = accountService.getAccount()
        return if (response.isSuccessful) {
            Log.i(TAG, "got account data successfully")
            Resource.success(response.body())
        } else {
            Resource.error("For your security, please login again.", null)
        }
    }

    suspend fun setupAccount(bankInstitutionEntities: List<BankInstitutionEntity>): Resource<User> {
        val response = accountService.setupAccount(bankInstitutionEntities)
        return if (response.isSuccessful) {
            Log.i(TAG, "setupAccount was a success")
            Resource.success(response.body())
        } else {
            Log.e(AuthRepository.TAG, "got error trying to set up user")
            Resource.error("There was an error setting up your account.", null)
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