package com.ercoding.proteintracker.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ercoding.proteintracker.data.local.PreferencesRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val dataStore: PreferencesRepository
) : ViewModel() {

    fun setProteinGoal(goal: Int) {
        viewModelScope.launch {
            dataStore.setProteinGoal(goal)
        }
    }
}