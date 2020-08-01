package org.elvor.translator.ui.main

import org.elvor.translator.service.TranslationResult

fun convert(translationResult: TranslationResult): List<QueryResultItem> {
    val result = ArrayList<QueryResultItem>(
        translationResult.options
            .sumBy { option -> option.items.size } + translationResult.options.size
    )
    for (option in translationResult.options) {
        result.add(
            QueryResultItem(
                option.name
            )
        )
        for ((index, item) in option.items.withIndex()) {
            result.add(
                QueryResultItem(
                    item.value,
                    index + 1,
                    item.subject
                )
            )
        }
    }
    return result
}