package com.ercoding.proteintracker.data.local

import com.ercoding.proteintracker.domain.PreferencesInterface
import com.ercoding.proteintracker.domain.ProteinEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesRepository() : PreferencesInterface {
    override val darkMode: Flow<Boolean>
        get() = flowOf(false)
    override val proteinGoal: Flow<Int?>
        get() = flowOf(150)
    override val dailyReached: Flow<Int?>
        get() = flowOf(50)
    override val proteinEntries: Flow<String?>
        get() = flowOf(null)

    override suspend fun setDarkMode(enabled: Boolean) {}

    override suspend fun setProteinGoal(proteinGoal: Int) {}

    override suspend fun setDailyReached(dailyReached: Int) {}

    override suspend fun setProteinEntries(entries: List<ProteinEntry>) {}

    override suspend fun getProteinEntries(): List<ProteinEntry> {
        return listOf()
    }
}