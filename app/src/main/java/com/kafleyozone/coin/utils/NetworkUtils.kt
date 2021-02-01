package com.kafleyozone.coin.utils

import okhttp3.Request
import java.util.*

// Base url
const val BASE_URL = "http://www.shinkansen.freeddns.org:8080/"

// Retrofit endpoints
const val EP_LOGIN = "auth/login"
const val EP_REGISTER = "auth/register"
const val EP_REFRESH = "auth/refresh"
const val EP_LOGOUT = "auth/logout"
const val EP_ACCOUNT = "api/account"

// Misc. constants
const val BEARER = "Bearer"
const val BASIC = "Basic"
const val HEADER_AUTHORIZATION = "Authorization"
const val HEADER_REFRESH = "Refresh-Token"

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