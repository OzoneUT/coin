package com.kafleyozone.coin.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBUser(
    @PrimaryKey
    var id: String,
    var name: String,
    var email: String,
    var created: String,
    var accountSetupComplete: Boolean,
    var setupAmount: Double,
)