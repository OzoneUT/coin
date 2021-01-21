package com.kafleyozone.coin.data.models

data class LoginResponse(
    val tokens: TokenResponse,
    val user: User
)