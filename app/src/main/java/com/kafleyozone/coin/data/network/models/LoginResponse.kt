package com.kafleyozone.coin.data.network.models

import com.kafleyozone.coin.data.domain.User

data class LoginResponse(
    val tokens: TokenResponse,
    val user: User
)