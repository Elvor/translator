package org.elvor.translator.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface QueryDao {
    @Query("SELECT * FROM `query` ORDER BY time DESC LIMIT 5")
    fun getHistory(): Single<List<org.elvor.translator.db.Query>>

    @Insert
    fun insertQuery(query: org.elvor.translator.db.Query): Completable
}