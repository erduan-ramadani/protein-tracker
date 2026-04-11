package com.ercoding.proteintracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ercoding.proteintracker.data.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val appModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single { androidContext().dataStore }
    single { PreferencesRepository(get()) }
}