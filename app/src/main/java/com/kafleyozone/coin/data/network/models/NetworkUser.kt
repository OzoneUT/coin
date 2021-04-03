package com.kafleyozone.coin.data.network.models


data class NetworkUser(
    val id: String,
    val name: String,
    val password: String,
    val email: String,
    val created: String,
    val accountSetupComplete: Boolean
)