package com.kafleyozone.coin.data.models

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)