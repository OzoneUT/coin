package com.kafleyozone.coin.data.domain

import com.kafleyozone.coin.data.room.models.DBUser
import com.kafleyozone.coin.data.room.models.SavedUser
import com.kafleyozone.coin.data.room.models.toBankInstitutionEntities

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val created: String,
    val accountSetupComplete: Boolean,
    val bankInstitutionEntities: List<BankInstitutionEntity>?,
) {
    companion object {
        fun from(savedUser: SavedUser): User {
            return User(
                id = savedUser.dbUser.id,
                name = savedUser.dbUser.name,
                email = savedUser.dbUser.email,
                password = "",
                created = savedUser.dbUser.created,
                accountSetupComplete = savedUser.dbUser.accountSetupComplete,
                bankInstitutionEntities = savedUser.dbBankInstitutionEntities.toBankInstitutionEntities()
            )
        }
    }
}

fun User.toDBUser(): DBUser {
    return DBUser(
        id = this.id,
        name = this.name,
        email = this.email,
        created = this.created,
        accountSetupComplete = this.accountSetupComplete,
    )
}
