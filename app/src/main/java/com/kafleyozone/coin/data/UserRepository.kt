package com.kafleyozone.coin.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val authStore: DataStore<Preferences>
) {
    companion object {
        const val TAG = "UserRepository"
        val KEY_USER_FLAG = stringPreferencesKey("user")
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun login(basicValue: String): Resource<LoginResponse> {
        val response = authenticationService.login(basicValue)
        val tokens = response.body()?.tokens
        val user = response.body()?.user?.email
        return if (response.isSuccessful && tokens != null && user != null) {
            authStore.edit { authStore ->
                authStore[KEY_USER_FLAG] = user
                authStore[KEY_ACCESS_TOKEN] = tokens.accessToken
                authStore[KEY_REFRESH_TOKEN] = tokens.refreshToken
            }
            Log.i(TAG, "new user written to dataStore: $user")
            Resource.success(response.body())
        } else {
            Resource.error(
                "We couldn't log you in. Check your username and password.", null)
        }
    }

    suspend fun getLocalUser(): String {
        return authStore.data.map { authStore ->
            authStore[KEY_USER_FLAG] ?: ""
        }.first()
    }
}