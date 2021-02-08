package com.kafleyozone.coin.data.network.models

data class NetworkBankInstitutionEntity(
    val id: String,
    val institutionName: String,
    val institutionType: String,
    val initialAmount: Double,
)