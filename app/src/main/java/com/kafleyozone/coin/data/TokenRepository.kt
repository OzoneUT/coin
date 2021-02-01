package com.kafleyozone.coin.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val authStore: DataStore<Preferences>
) {

    companion object {
        val KEY_USER_FLAG = stringPreferencesKey("user")
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun cacheNewAuth(accessToken: String, refreshToken: String, email: String = "") {
        authStore.edit { authStore ->
            authStore[KEY_USER_FLAG] = email
            authStore[KEY_ACCESS_TOKEN] = accessToken
            authStore[KEY_REFRESH_TOKEN] = refreshToken
        }
        Log.i(AuthRepository.TAG, "auth updated")
    }

    suspend fun getCachedUserEmail(): String {
        return authStore.data.map { authStore ->
            authStore[KEY_USER_FLAG] ?: ""
        }.first()
    }

    suspend fun getCachedAccessToken(): String {
        return authStore.data.map { authStore ->
            authStore[KEY_ACCESS_TOKEN] ?: ""
        }.first()
    }

    suspend fun getCachedRefreshToken(): String {
        return authStore.data.map { authStore ->
            authStore[KEY_REFRESH_TOKEN] ?: ""
        }.first()
    }
}