package com.kafleyozone.coin.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kafleyozone.coin.data.room.models.DBBankInstitutionEntity
import com.kafleyozone.coin.data.room.models.DBUser

@Database(
    entities = [DBUser::class, DBBankInstitutionEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}