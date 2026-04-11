package com.ercoding.proteintracker.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ercoding.proteintracker.data.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(pref: PreferencesRepository) : ViewModel() {

    val isOnboardingComplete = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    init {
        viewModelScope.launch {
            isLoading.value = true
            val proteinGoal = pref.proteinGoal.first()
            isOnboardingComplete.value = proteinGoal != null
            isLoading.value = false
        }
    }
}