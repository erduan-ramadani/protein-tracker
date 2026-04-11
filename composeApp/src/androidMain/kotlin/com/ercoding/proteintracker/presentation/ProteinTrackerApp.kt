package com.ercoding.proteintracker.presentation

import android.app.Application
import com.ercoding.proteintracker.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ProteinTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ProteinTrackerApp)
            androidLogger()
        }
    }
}