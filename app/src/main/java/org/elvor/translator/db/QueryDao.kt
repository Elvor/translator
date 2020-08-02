package org.elvor.translator.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface QueryDao {
    @Query("SELECT * FROM `query` ORDER BY time DESC")
    fun getHistory(): Single<List<org.elvor.translator.db.Query>>

    @Query("DELETE FROM `query` WHERE time <= (SELECT time FROM `query` ORDER BY time DESC LIMIT 1 OFFSET 5)")
    fun cleanOldQueries() : Completable

    @Insert
    fun insertQuery(query: org.elvor.translator.db.Query): Completable
}