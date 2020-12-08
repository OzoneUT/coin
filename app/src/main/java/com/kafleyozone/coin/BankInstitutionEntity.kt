package com.kafleyozone.coin

import java.util.*


data class BankInstitutionEntity(val institutionName: String,
                                 val institutionType: String,
                                 val amount: Double,
                                 val id: String = UUID.randomUUID().toString()) {

    enum class Type {
        Checking, Savings, Cash
    }
}