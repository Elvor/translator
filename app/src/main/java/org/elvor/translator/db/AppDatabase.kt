package org.elvor.translator.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Query::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queryDao(): QueryDao
}