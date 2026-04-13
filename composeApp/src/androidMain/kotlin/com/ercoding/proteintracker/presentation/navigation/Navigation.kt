package com.ercoding.proteintracker.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ercoding.proteintracker.presentation.MainViewModel
import com.ercoding.proteintracker.presentation.dashboard.DashboardScreen
import com.ercoding.proteintracker.presentation.onboarding.OnboardingScreen
import com.ercoding.proteintracker.presentation.settings.SettingsScreen
import com.ercoding.proteintracker.presentation.theme.ProteinTrackerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation() {

    val mainViewModel: MainViewModel = koinViewModel()
    val navController = rememberNavController()
    var startDestination: String
    val isDarkMode by mainViewModel.isDarkMode.collectAsState()

    ProteinTrackerTheme(darkTheme = isDarkMode) {
        startDestination =
            if (mainViewModel.isOnboardingComplete.value) {
                Routes.dashboard
            } else {
                Routes.onboarding
            }
        if (mainViewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                )
            }
        } else {
            NavHost(navController, startDestination) {
                composable(Routes.onboarding) {
                    OnboardingScreen(
                        onConfirmClick = { navController.navigate(Routes.dashboard) }
                    )
                }
                composable(Routes.dashboard) {
                    DashboardScreen(
                        onSettingsClick = { navController.navigate(Routes.settings) }
                    )
                }
                composable(Routes.settings) {
                    SettingsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}