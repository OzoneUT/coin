package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.domain.toDBUser
import com.kafleyozone.coin.data.network.AccountService
import com.kafleyozone.coin.data.network.models.NetworkSetupAmountEntity
import com.kafleyozone.coin.data.network.models.Resource
import com.kafleyozone.coin.data.network.models.toDomainUser
import com.kafleyozone.coin.data.room.UserDao
import okhttp3.ResponseBody
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val accountService: AccountService,
    private val userDao: UserDao,
    private val tokenRepository: TokenRepository
) {

    companion object {
        private const val TAG = "AppRepository"
    }

    suspend fun getAccount(): Resource<User> {
        val response = accountService.getAccount()
        val user = response.body()?.toDomainUser()
        return if (response.isSuccessful) {
            Log.i(TAG, "got account data successfully")
            if (user != null) {
                userDao.clearUser()
                userDao.insertUser(user.toDBUser())
                Resource.success(user)
            } else {
                Resource.error("There was an error getting your account details.", null)
            }
        } else {
            Resource.error("For your security, please login again.", null)
        }
    }

    suspend fun getAuthorizedUserFromDB(id: String): User? {
        val savedUser = userDao.getUser(id)
        return savedUser?.let { User.from(it) }
    }

    suspend fun setupAccount(networkSetupAmountEntity: NetworkSetupAmountEntity): Resource<User> {
        val response = accountService.setupAccount(networkSetupAmountEntity)
        val user = response.body()?.toDomainUser()
        return if (response.isSuccessful) {
            Log.i(TAG, "setupAccount was a success")

            userDao.clearUser()
            user?.toDBUser()?.let { userDao.insertUser(it) }
            Resource.success(user)
        } else {
            Log.e(AuthRepository.TAG, "got error trying to set up user")
            Resource.error("There was an error setting up your account.", null)
        }
    }

    suspend fun logout(): Resource<ResponseBody> {
        val response = accountService.logout()
        userDao.clearUser()
        tokenRepository.deleteCachedAuth()
        return if (response.isSuccessful) {
            Log.i(AuthRepository.TAG, "logged user out successfully")
            Resource.success(null)
        } else {
            Log.e(AuthRepository.TAG, "got error trying to log out user")
            Resource.error("", null)
        }
    }
}