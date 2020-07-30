package org.elvor.translator.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.elvor.translator.App
import org.elvor.translator.R
import org.elvor.translator.di.MainComponent
import org.elvor.translator.ui.main.query.QueryFragment

class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        mainComponent = (application as App).appComponent.mainComponent().create(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QueryFragment())
            .commit()
    }
}