package com.kafleyozone.coin.data.room.models

import androidx.room.Embedded
import androidx.room.Relation
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.domain.toDBBankInstitutionEntities
import com.kafleyozone.coin.data.domain.toDBUser

data class SavedUser(
    @Embedded val dbUser: DBUser,
    @Relation(parentColumn = "id", entityColumn = "userId")
    val dbBankInstitutionEntities: List<DBBankInstitutionEntity>?
) {
    companion object {
        fun fromUser(user: User): SavedUser {
            return SavedUser(
                user.toDBUser(),
                user.bankInstitutionEntities?.toDBBankInstitutionEntities(user.id)
            )
        }
    }
}