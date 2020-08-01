package org.elvor.translator.ui.main.query

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import org.elvor.translator.service.TranslationService
import org.elvor.translator.ui.main.MainRouter
import org.elvor.translator.ui.main.TranslationResultView
import org.elvor.translator.ui.main.convert
import javax.inject.Inject

interface QueryView : MvpView, TranslationResultView {
    @AddToEndSingle
    fun disableTargetLanguage(languageId: Int)
}

class QueryPresenter @Inject constructor(
    private val translationService: TranslationService,
    private val mainRouter: MainRouter
) : MvpPresenter<QueryView>() {
    fun translate(query: String, sourceLanguage: Int, targetLanguage: Int) {
        viewState.showLoading()
        translationService.translate(query, sourceLanguage, targetLanguage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showResult(convert(it))
                viewState.hideLoading()
            }, {
                viewState.showError(it.message)
                viewState.hideLoading()
            })
    }

    fun onSourceLanguageSelected(languageId: Int) {
        viewState.disableTargetLanguage(languageId)
    }

    fun openHistory() {
        mainRouter.openHistory()
    }
}