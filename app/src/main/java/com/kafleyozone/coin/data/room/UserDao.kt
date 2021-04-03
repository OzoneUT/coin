package com.kafleyozone.coin.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kafleyozone.coin.data.room.models.DBUser
import com.kafleyozone.coin.data.room.models.SavedUser

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM DBUser WHERE id = :id LIMIT 1")
    suspend fun getUser(id: String): SavedUser?

    @Insert
    suspend fun insertUser(dbUser: DBUser)

    @Query("DELETE FROM DBUser")
    suspend fun clearUser()
}
