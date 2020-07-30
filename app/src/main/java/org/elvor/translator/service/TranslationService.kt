package org.elvor.translator.service

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.elvor.translator.backend.TranslationServiceApi
import org.jsoup.Jsoup
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TranslationService @Inject constructor(private val translationServiceApi: TranslationServiceApi) {

    //TODO fix streams
    fun translate(
        query: String,
        sourceLanguageId: Int,
        targetLanguageId: Int
    ): Observable<TranslationResult> {
        return translationServiceApi.getTranslations(query, sourceLanguageId, targetLanguageId)
            .subscribeOn(Schedulers.io())
            .map { content ->
                val doc = Jsoup.parse(content.string())
                val tables = doc.select("table[width=\"100%\"]")
                if (tables.size == 0) {
                    return@map TranslationResult(emptyList())
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
                return@map TranslationResult(ArrayList(options))
            }
    }
}