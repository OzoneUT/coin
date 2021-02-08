package com.kafleyozone.coin.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafleyozone.coin.data.domain.BankInstitutionEntity

@Entity
data class DBBankInstitutionEntity(
    @PrimaryKey
    var id: String,
    var userId: String,
    var institutionName: String,
    var institutionType: String,
    var initialAmount: Double
)

fun List<DBBankInstitutionEntity>?.toBankInstitutionEntities(): List<BankInstitutionEntity>? {
    if (this == null) return null
    val result = mutableListOf<BankInstitutionEntity>()
    for (dbEntity in this) {
        result.add(
            BankInstitutionEntity(
                id = dbEntity.id,
                institutionName = dbEntity.institutionName,
                institutionType = dbEntity.institutionType,
                initialAmount = dbEntity.initialAmount
            )
        )
    }
    return result
}