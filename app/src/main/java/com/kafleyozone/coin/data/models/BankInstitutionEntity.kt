package com.kafleyozone.coin.data.models

import java.util.*


data class BankInstitutionEntity(
        val institutionName: String,
        val institutionType: String,
        val initialAmount: Double,
        val id: String = UUID.randomUUID().toString(),
) {

    enum class Type {
        Checking, Savings, Cash
    }
}