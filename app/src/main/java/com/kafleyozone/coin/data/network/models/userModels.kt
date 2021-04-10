package com.kafleyozone.coin.data.network.models

import com.kafleyozone.coin.data.domain.User


data class NetworkUser(
    val id: String,
    val name: String,
    val password: String,
    val email: String,
    val created: String,
    val accountSetupComplete: Boolean,
    val setupAmount: Double,
)

fun NetworkUser.toDomainUser(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        created = this.created,
        accountSetupComplete = this.accountSetupComplete,
        setupAmount = this.setupAmount
    )
}

data class NetworkSetupAmountEntity(
    val setupAmount: Double
)