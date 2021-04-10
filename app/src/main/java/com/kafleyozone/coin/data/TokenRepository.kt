package com.kafleyozone.coin.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafleyozone.coin.utils.MOCK_DEBUG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val authStore: DataStore<Preferences>
) {

    companion object {
        private const val TAG = "TokenRepository"
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
        Log.i(TAG, "auth updated")
    }

    suspend fun deleteCachedAuth() {
        authStore.edit { authStore ->
            authStore[KEY_ACCESS_TOKEN] = ""
            authStore[KEY_REFRESH_TOKEN] = ""
        }
        Log.i(TAG, "cached auth tokens emptied")
    }

    suspend fun getCachedUserEmail(): String {
        return authStore.data.map { authStore ->
            authStore[KEY_USER_FLAG] ?: ""
        }.first()
    }

    suspend fun getCachedAccessToken(): String {
        if (MOCK_DEBUG) return "mock_access_token"
        return authStore.data.map { authStore ->
            authStore[KEY_ACCESS_TOKEN] ?: ""
        }.first()
    }

    suspend fun getCachedRefreshToken(): String {
        if (MOCK_DEBUG) return "mock_refresh_token"
        return authStore.data.map { authStore ->
            authStore[KEY_REFRESH_TOKEN] ?: ""
        }.first()
    }
}