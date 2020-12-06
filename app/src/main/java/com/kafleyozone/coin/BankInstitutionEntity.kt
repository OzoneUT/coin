package com.kafleyozone.coin


data class BankInstitutionEntity(val institutionName: String,
                                 val institutionType: String,
                                 val amount: Double) {
    enum class Type {
        Checking, Savings, Cash
    }
}