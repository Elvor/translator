package org.elvor.translator.ui.main.history

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import org.elvor.translator.service.HistoryService
import org.elvor.translator.ui.main.MainRouter
import javax.inject.Inject

interface HistoryView : MvpView {
    @AddToEndSingle
    fun showHistory(history: List<HistoryItem>)

    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()
}

class HistoryPresenter @Inject constructor(
    private val historyService: HistoryService,
    private val mainRouter: MainRouter
) :
    MvpPresenter<HistoryView>() {
    fun fetchHistory() {
        viewState.showLoading()
        historyService.fetchHistory()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { history ->
                viewState.showHistory(history.map { query ->
                    HistoryItem(
                        query.query,
                        query.srcLangId,
                        query.trgLangId
                    )
                })
                viewState.hideLoading()
            }
    }

    fun openHistoryItem(item: HistoryItem) {
        mainRouter.openHistoryItem(item)
    }
}