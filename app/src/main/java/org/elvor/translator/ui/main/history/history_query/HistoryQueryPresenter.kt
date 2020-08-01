package org.elvor.translator.ui.main.history.history_query

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import org.elvor.translator.service.TranslationService
import org.elvor.translator.ui.main.TranslationResultView
import org.elvor.translator.ui.main.convert
import javax.inject.Inject

interface HistoryQueryView : MvpView, TranslationResultView {
    @AddToEndSingle
    fun fillQueryInfo(query: String, sourceLanguageId: Int, targetLanguageId: Int)
}

class HistoryQueryPresenter @Inject constructor(private val translationService: TranslationService) : MvpPresenter<HistoryQueryView>() {
    fun fillQueryInfo(query: String, sourceLanguageId: Int, targetLanguageId: Int) {
        viewState.fillQueryInfo(query, sourceLanguageId, targetLanguageId)
        viewState.showLoading()
        translationService.translate(query, sourceLanguageId, targetLanguageId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                with(viewState) {
                    showResult(convert(it))
                    hideLoading()
                }
            }, {
                with(viewState) {
                    showError(it.message)
                    hideLoading()
                }
            })
    }
}