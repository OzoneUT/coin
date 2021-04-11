package com.kafleyozone.coin.data.room.models

import androidx.room.Embedded
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.domain.toDBUser

data class SavedUser(
    @Embedded val dbUser: DBUser
) {
    companion object {
        fun fromUser(user: User): SavedUser {
            return SavedUser(
                user.toDBUser()
            )
        }
    }
}