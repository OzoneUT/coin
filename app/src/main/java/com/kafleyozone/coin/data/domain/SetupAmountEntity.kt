package com.kafleyozone.coin.data.domain

import java.util.*

data class SetupAmountEntity(
    val amount: Double,
    val id: String = UUID.randomUUID().toString()
)