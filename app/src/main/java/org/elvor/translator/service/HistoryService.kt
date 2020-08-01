package org.elvor.translator.service

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.elvor.translator.db.AppDatabase
import org.elvor.translator.db.Query
import javax.inject.Inject

class HistoryService @Inject constructor(private val appDatabase: AppDatabase) {
    fun fetchHistory(): Observable<List<Query>> {
        return appDatabase.queryDao().getHistory().toObservable()
            .subscribeOn(Schedulers.io())
    }
}