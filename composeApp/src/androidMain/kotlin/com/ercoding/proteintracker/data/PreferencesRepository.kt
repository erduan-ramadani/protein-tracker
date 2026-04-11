package com.ercoding.proteintracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val PROTEIN_GOAL = intPreferencesKey("protein_goal")
    }

    val darkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    val proteinGoal: Flow<Int?> = dataStore.data.map { it[PROTEIN_GOAL] }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    suspend fun setProteinGoal(proteinGoal: Int) {
        dataStore.edit { it[PROTEIN_GOAL] = proteinGoal }
    }
}