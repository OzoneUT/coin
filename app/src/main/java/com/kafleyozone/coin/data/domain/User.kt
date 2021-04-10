package com.kafleyozone.coin.data.domain

import com.kafleyozone.coin.data.room.models.DBUser
import com.kafleyozone.coin.data.room.models.SavedUser

data class User(
    val id: String,
    val name: String,
    val email: String,
    val created: String,
    val accountSetupComplete: Boolean,
    val setupAmount: Double,
) {
    companion object {
        fun from(savedUser: SavedUser): User {
            return User(
                id = savedUser.dbUser.id,
                name = savedUser.dbUser.name,
                email = savedUser.dbUser.email,
                created = savedUser.dbUser.created,
                accountSetupComplete = savedUser.dbUser.accountSetupComplete,
                setupAmount = savedUser.dbUser.setupAmount,
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
        setupAmount = this.setupAmount
    )
}
