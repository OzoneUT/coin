package com.kafleyozone.coin.data.models

import java.io.Serializable

data class User(
     val id: String,
     val name: String,
     val email: String,
     val password: String,
     val created: String
) : Serializable