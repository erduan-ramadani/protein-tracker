package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ercoding.proteintracker.data.local.PreferencesRepository
import com.ercoding.proteintracker.data.remote.AnthropicRepository
import com.ercoding.proteintracker.domain.ProteinEntry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class DashboardViewModel(
    private val anthropicRepo: AnthropicRepository,
    private val prefRepository: PreferencesRepository
) : ViewModel() {

    val dailyReached: Int get() = getDailyReached(selectedDate)
    val dailyGoal = prefRepository.proteinGoal.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )
    var proteinEntries = mutableStateListOf<ProteinEntry>()

    val proteinEntriesByDate: Map<LocalDate, List<ProteinEntry>>
        get() = proteinEntries.groupBy { entry ->
            Instant.ofEpochMilli(entry.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }.toSortedMap()

    val last7Days: List<LocalDate> = (0..6).map {
        LocalDate.now().minusDays(it.toLong())
    }.reversed()
    var selectedDate: LocalDate? by mutableStateOf(LocalDate.now())

    var isLoading by mutableStateOf(false)
    val progress: Float
        get() =
            if ((dailyGoal.value ?: 0) == 0) 0f
            else dailyReached.toFloat() / (dailyGoal.value ?: 0)

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            proteinEntries.addAll(prefRepository.getProteinEntries())
            proteinEntries.addAll(
                listOf(
                    ProteinEntry(
                        meal = "Test vorgestern",
                        proteinAmount = 30,
                        createdAt = System.currentTimeMillis() - 166400000
                    ),
                    ProteinEntry(
                        meal = "Test gestern",
                        proteinAmount = 30,
                        createdAt = System.currentTimeMillis() - 86400000
                    ),
                    ProteinEntry(
                        meal = "Test heute",
                        proteinAmount = 50,
                        createdAt = System.currentTimeMillis()
                    )
                )
            )
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
            prefRepository.setDailyReached(dailyReached)
            proteinEntries.clear()
            prefRepository.setProteinEntries(proteinEntries)
        }
    }

    fun removeProteinEntry(entry: ProteinEntry) {
        viewModelScope.launch {
            prefRepository.setDailyReached(dailyReached)

            proteinEntries.removeIf { it.id == entry.id }
            prefRepository.setProteinEntries(proteinEntries)
        }
    }

    fun getDailyReached(date: LocalDate?): Int {
        return proteinEntriesByDate[date]?.sumOf { it.proteinAmount } ?: 0
    }
}