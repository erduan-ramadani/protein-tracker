package com.ercoding.proteintracker.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ercoding.proteintracker.presentation.MainViewModel
import com.ercoding.proteintracker.presentation.dashboard.DashboardScreen
import com.ercoding.proteintracker.presentation.onboarding.OnboardingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation() {

    val mainViewModel: MainViewModel = koinViewModel()
    val navController = rememberNavController()
    var startDestination: String

    MaterialTheme() {
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
                    DashboardScreen()
                }
            }
        }
    }
}