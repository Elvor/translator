package org.elvor.translator.ui.main.query

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import org.elvor.translator.service.TranslationResult
import org.elvor.translator.service.TranslationService
import javax.inject.Inject

interface QueryView : MvpView {

    @AddToEndSingle
    fun showResult(items: List<QueryResultItem>)

    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()

    @AddToEndSingle
    fun disableTargetLanguage(languageId: Int)

    @AddToEndSingle
    fun showError(message: String?)
}

class QueryPresenter @Inject constructor(private val translationService: TranslationService) :
    MvpPresenter<QueryView>() {

    fun translate(query: String, sourceLanguage: Int, targetLanguage: Int) {
        viewState.showLoading()
        translationService.translate(query, sourceLanguage, targetLanguage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ( { t: TranslationResult ->
                val result =
                    ArrayList<QueryResultItem>(t.options.sumBy { option -> option.items.size } + t.options.size)
                for (option in t.options) {
                    result.add(QueryResultItem(option.name))
                    for ((index, item) in option.items.withIndex()) {
                        result.add(QueryResultItem(item.value, index + 1, item.subject))
                    }
                }
                viewState.showResult(result)
                viewState.hideLoading()
            }, {
                    error ->
                viewState.showError(error.message)
                viewState.hideLoading()
            })
    }

    fun onSourceLanguageSelected(languageId: Int) {
        viewState.disableTargetLanguage(languageId)
    }

}