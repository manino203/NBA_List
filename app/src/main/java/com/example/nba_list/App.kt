package com.example.nba_list

import android.app.Application
import com.example.nba_list.data.network.RetrofitClient
import com.example.nba_list.di.appModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}
