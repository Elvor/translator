package org.elvor.translator.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Query(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "query") var query: String,
    @ColumnInfo(name = "srcLangId") var srcLangId: Int,
    @ColumnInfo(name = "trgLangId") var trgLangId: Int,
    @ColumnInfo(name = "time") var time: Long
)