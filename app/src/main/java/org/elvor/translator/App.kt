package org.elvor.translator

import android.app.Application
import org.elvor.translator.di.AppComponent
import org.elvor.translator.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.create()
    }
}