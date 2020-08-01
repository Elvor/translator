package org.elvor.translator.ui.main

import moxy.viewstate.strategy.alias.AddToEndSingle

interface TranslationResultView {
    @AddToEndSingle
    fun showResult(items: List<QueryResultItem>)

    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()

    @AddToEndSingle
    fun showError(message: String?)
}