package org.elvor.translator.di

import dagger.BindsInstance
import dagger.Subcomponent
import org.elvor.translator.ui.main.MainActivity
import org.elvor.translator.ui.main.history.HistoryFragment
import org.elvor.translator.ui.main.history.history_query.HistoryQueryFragment
import org.elvor.translator.ui.main.query.QueryFragment

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance activity: MainActivity): MainComponent
    }

    fun inject(queryFragment: QueryFragment)
    fun inject(queryFragment: HistoryFragment)
    fun inject(historyQueryFragment: HistoryQueryFragment)
}