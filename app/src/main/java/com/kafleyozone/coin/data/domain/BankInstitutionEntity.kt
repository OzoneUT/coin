package com.kafleyozone.coin.data.domain

import com.kafleyozone.coin.data.room.models.DBBankInstitutionEntity
import java.util.*


data class BankInstitutionEntity(
    val institutionName: String,
    val institutionType: String,
    val initialAmount: Double,
    val id: String = UUID.randomUUID().toString()
) {

    enum class Type {
        Checking, Savings, Cash
    }
}

fun List<BankInstitutionEntity>.toDBBankInstitutionEntities(userId: String):
        List<DBBankInstitutionEntity> {
    val result = mutableListOf<DBBankInstitutionEntity>()
    for (elem in this) {
        result.add(
            DBBankInstitutionEntity(
                id = elem.id,
                userId = userId,
                institutionName = elem.institutionName,
                institutionType = elem.institutionType,
                initialAmount = elem.initialAmount
            )
        )
    }
    return result.toList()
}