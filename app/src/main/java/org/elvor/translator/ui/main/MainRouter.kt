package org.elvor.translator.ui.main

import org.elvor.translator.R
import org.elvor.translator.ui.main.history.HistoryFragment
import org.elvor.translator.ui.main.history.HistoryItem
import org.elvor.translator.ui.main.history.history_query.HistoryQueryFragment
import javax.inject.Inject

class MainRouter @Inject constructor(private val mainActivity: MainActivity) {
    fun openHistory() {
        mainActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HistoryFragment())
            .addToBackStack("history")
            .commit()
    }

    fun openHistoryItem(item: HistoryItem) {
        mainActivity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                HistoryQueryFragment.newInstance(
                    item.query,
                    item.sourceLanguageId,
                    item.targetLanguageId
                )
            )
            .addToBackStack("history_item")
            .commit()
    }
}