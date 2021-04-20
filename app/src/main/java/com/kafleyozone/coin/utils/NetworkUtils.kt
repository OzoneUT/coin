package com.kafleyozone.coin.utils

import okhttp3.Request
import java.util.*

// Base urls
const val BASE_URL = "http://10.0.2.2:8080/" //10.0.2.2 to access host machine's loopback address
const val MOCK_BASE_URL = "https://f4ceca4f-5f0f-4f72-bcb7-533e484397af.mock.pstmn.io"

// Retrofit endpoints
const val EP_LOGIN = "auth/login"
const val EP_REGISTER = "auth/register"
const val EP_REFRESH = "auth/refresh"
const val EP_LOGOUT = "auth/logout"
const val EP_ACCOUNT = "api/account"
const val EP_ACCOUNT_SETUP = "api/setup"

// Misc. constants
const val BEARER = "Bearer"
const val BASIC = "Basic"
const val HEADER_AUTHORIZATION = "Authorization"
const val HEADER_REFRESH = "Refresh-Token"
const val FLAG_BAD_REQUEST = "Coin_x69Cf58pFB_Flag"

// Network statuses for UI
enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}

fun hasAuthHeader(request: Request): Boolean {
    val header = request.header(HEADER_AUTHORIZATION)
    return header?.isNotEmpty() ?: false
}

fun usingBasicAuth(request: Request): Boolean {
    val header = request.header(HEADER_AUTHORIZATION)
    return header?.startsWith(BASIC) ?: false
}

fun isRefreshingToken(request: Request): Boolean {
    val header = request.header(HEADER_REFRESH)
    return header?.toLowerCase(Locale.ROOT)?.equals("true") ?: false
}

fun toBearerToken(token: String) = "$BEARER $token"