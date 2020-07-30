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
}

class QueryPresenter @Inject constructor(private val translationService: TranslationService) :
    MvpPresenter<QueryView>() {

    fun translate(query: String) {
        viewState.showLoading()
        translationService.translate(query, 3, 2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t: TranslationResult ->
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
            }
    }

}