package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import java.util.UUID

class DashboardViewModel(
    private val anthropicRepo: AnthropicRepository,
    private val prefRepository: PreferencesRepository
) : ViewModel() {

    var dailyReached by mutableIntStateOf(0)
    var dailyGoal by mutableIntStateOf(0)
    var proteinEntries = mutableStateListOf<ProteinEntry>()
    var isLoading by mutableStateOf(false)
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
            isLoading = true
            val result = anthropicRepo.requestProteinAmount(query)
            result.onFailure {
                _events.send("Unbekannter Fehler")
            }
            result.onSuccess { proteinAmount ->
                dailyReached += proteinAmount
                proteinEntries += ProteinEntry(
                    UUID.randomUUID().toString(),
                    query,
                    proteinAmount
                )
                prefRepository.setDailyReached(dailyReached)
                prefRepository.setProteinEntries(proteinEntries)
            }
            isLoading = false
        }
    }

    fun reset() {
        viewModelScope.launch {
            dailyReached = 0
            prefRepository.setDailyReached(dailyReached)

            proteinEntries.clear()
            prefRepository.setProteinEntries(proteinEntries)
        }
    }

    fun removeProteinEntry(entry: ProteinEntry) {
        viewModelScope.launch {
            dailyReached -= entry.proteinAmount
            prefRepository.setDailyReached(dailyReached)

            proteinEntries.removeIf { it.id == entry.id }
            prefRepository.setProteinEntries(proteinEntries)
        }
    }
}