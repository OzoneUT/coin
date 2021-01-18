package com.kafleyozone.coin.utils

// Base url
const val BASE_URL = "http://localhost:8080/"

// Retrofit endpoints
const val EP_LOGIN = "auth/login"
const val EP_REFRESH = "auth/refresh"
const val EP_LOGOUT = "auth/logout"

// Network statuses for UI
enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}