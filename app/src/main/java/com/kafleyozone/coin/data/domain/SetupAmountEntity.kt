package com.kafleyozone.coin.data.domain

import com.kafleyozone.coin.data.network.models.NetworkSetupAmountEntity
import java.util.*

data class SetupAmountEntity(
    val amount: Double,
    val id: String = UUID.randomUUID().toString()
)

fun SetupAmountEntity.toNetworkSetupAmountEntity(): NetworkSetupAmountEntity {
    return NetworkSetupAmountEntity(setupAmount = this.amount)
}