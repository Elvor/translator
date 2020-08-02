package org.elvor.translator.service

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.elvor.translator.backend.TranslationServiceApi
import org.elvor.translator.db.AppDatabase
import org.elvor.translator.db.Query
import org.jsoup.Jsoup
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TranslationService @Inject constructor(
    private val translationServiceApi: TranslationServiceApi,
    private val appDatabase: AppDatabase
) {

    fun fetchTranslation(
        query: String,
        sourceLanguageId: Int,
        targetLanguageId: Int
    ): Observable<TranslationResult> {
        return translationServiceApi.getTranslations(
            query,
            sourceLanguageId,
            targetLanguageId
        )
            .subscribeOn(Schedulers.io())
            .map { parse(it) }
    }

    private fun parse(responseBody: ResponseBody): TranslationResult {
        val doc = Jsoup.parse(responseBody.string())
        val tables = doc.select("table[width=\"100%\"]")
        if (tables.size == 0) {
            return TranslationResult(emptyList())
        }
        val resultTable = tables[0]
        val items = resultTable.child(0).children()
        var currentOptionName = ""
        var currentOptionItems = LinkedList<TranslationResultItem>()
        val options = LinkedList<TranslationResultOption>()
        for (item in items) {
            if (item.children().size == 1) {
                val child = item.child(0)
                val attr = child.attr("class", "gray")
                if (attr != null) {
                    if (currentOptionItems.size > 0) {
                        options.addLast(
                            TranslationResultOption(
                                currentOptionName,
                                ArrayList(currentOptionItems)
                            )
                        )
                    }
                    currentOptionItems = LinkedList()
                    currentOptionName = child.html()
                }
            }
            if (item.children().size == 2) {
                currentOptionItems.addLast(
                    TranslationResultItem(
                        item.child(0).text(),
                        item.child(1).html()
                    )
                )
            }
        }
        if (currentOptionItems.size > 0) {
            options.addLast(
                TranslationResultOption(
                    currentOptionName,
                    ArrayList(currentOptionItems)
                )
            )
        }
        return TranslationResult(ArrayList(options))
    }

    fun fetchTranslationAddingToHistory(
        query: String,
        sourceLanguageId: Int,
        targetLanguageId: Int
    ): Observable<TranslationResult> {
        return appDatabase.queryDao().insertQuery(
            Query(
                null,
                query,
                sourceLanguageId,
                targetLanguageId,
                System.currentTimeMillis()
            )
        )
            .subscribeOn(Schedulers.io())
            .andThen(appDatabase.queryDao().cleanOldQueries())
            .andThen(fetchTranslation(query, sourceLanguageId, targetLanguageId))
    }
}