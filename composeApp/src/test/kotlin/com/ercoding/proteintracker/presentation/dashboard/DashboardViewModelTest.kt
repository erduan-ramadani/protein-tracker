@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ercoding.proteintracker.presentation.dashboard

import com.ercoding.proteintracker.data.local.FakePreferencesRepository
import com.ercoding.proteintracker.data.remote.FakeAnthropicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DashboardViewModelTest {

    private val fakeAnthropicRepo = FakeAnthropicRepository()
    private val fakePreferencesRepo = FakePreferencesRepository()
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = DashboardViewModel(fakeAnthropicRepo, fakePreferencesRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProteins_increases_dailyReached() = runTest {
        // Arrange: dailyReached startet bei 0, FakeRepo gibt immer 3 zurück
        // Act — Aktion ausführen
        viewModel.addProteins("100g hähnchen")
        advanceUntilIdle()
        println("dailyReached: ${viewModel.dailyReached}")
        println("entries: ${viewModel.proteinEntries}")
        // Assert — Ergebnis prüfen
        assert(viewModel.dailyReached == 35)
    }
}