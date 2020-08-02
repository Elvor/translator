package org.elvor.translator.ui.main

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface TranslationResultView {
    @AddToEndSingle
    fun showResult(items: List<QueryResultItem>)

    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()

    @Skip
    fun showError(message: String?)
}