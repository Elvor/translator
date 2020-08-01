package org.elvor.translator.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.elvor.translator.backend.BackendModule
import org.elvor.translator.db.DBModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, BackendModule::class, DBModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

    fun mainComponent(): MainComponent.Factory
}