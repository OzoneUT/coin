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

// Network statuses for UI
enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}

fun hasAuthHeader(request: Request): Boolean {
    val header = request.header("Authorization")
    return header?.isNotEmpty() ?: false
}

fun usingBasicAuth(request: Request): Boolean {
    val header = request.header("Authorization")
    return header?.startsWith("Basic") ?: false
}

fun isRefreshingToken(request: Request): Boolean {
    val header = request.header("Refresh-Token")
    return header?.toLowerCase(Locale.ROOT)?.equals("true") ?: false
}