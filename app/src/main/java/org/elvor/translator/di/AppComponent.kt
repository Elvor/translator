package org.elvor.translator.di

import dagger.Component
import org.elvor.translator.backend.BackendModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, BackendModule::class])
interface AppComponent {

    fun mainComponent(): MainComponent.Factory
}