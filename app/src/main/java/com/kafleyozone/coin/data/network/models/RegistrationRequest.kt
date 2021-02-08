package com.kafleyozone.coin.data.network.models

data class RegistrationRequest(
        val name: String,
        val email: String,
        val password: String
)