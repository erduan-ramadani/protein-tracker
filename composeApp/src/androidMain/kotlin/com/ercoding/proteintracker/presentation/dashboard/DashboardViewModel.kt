package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ercoding.proteintracker.data.local.PreferencesRepository
import com.ercoding.proteintracker.data.remote.AnthropicRepository
import com.ercoding.proteintracker.domain.ProteinEntry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val anthropicRepo: AnthropicRepository,
    private val prefRepository: PreferencesRepository
) : ViewModel() {

    var mealAmount by mutableIntStateOf(0)
    var dailyReached by mutableIntStateOf(0)
    var dailyGoal by mutableIntStateOf(0)
    var proteinEntries = mutableStateListOf<ProteinEntry>()
    val progress: Float get() = if (dailyGoal == 0) 0f else dailyReached.toFloat() / dailyGoal

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            dailyGoal = prefRepository.proteinGoal.first() ?: 0
            dailyReached = prefRepository.dailyReached.first() ?: 0
            proteinEntries.addAll(prefRepository.getProteinEntries())
        }
    }

    fun addProteins(query: String) {
        viewModelScope.launch {
            val result = anthropicRepo.requestProteinAmount(query)
            result.onFailure {
                _events.send("Unbekannter Fehler")
            }
            result.onSuccess { proteinAmount ->
                mealAmount = proteinAmount
                dailyReached += mealAmount
                proteinEntries += ProteinEntry(query, mealAmount)

                prefRepository.setDailyReached(dailyReached)
                prefRepository.setProteinEntries(proteinEntries)
            }
        }
    }
}