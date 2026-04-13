package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onSettingsClick: () -> Unit
) {

    val viewModel: DashboardViewModel = koinViewModel()

    val dailyReached = viewModel.dailyReached
    val dailyGoal = viewModel.dailyGoal.collectAsState()
    val progress = viewModel.progress
    val dailyEntries = viewModel.proteinEntries

    val snackbarHostState = remember { SnackbarHostState() }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { apiResponse ->
            snackbarHostState.showSnackbar(
                message = apiResponse,
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ProteinTracker",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            OutlinedButton(
                onClick = {
                    viewModel.reset()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Zurücksetzen")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(70.dp),
                progress = { progress }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Tageswert: $dailyReached / ${dailyGoal.value}")
            Spacer(modifier = Modifier.padding(8.dp))
            ProteinInputSection(
                onClick = { query ->
                    viewModel.addProteins(query)
                },
                isLoading = viewModel.isLoading
            )
            Spacer(modifier = Modifier.padding(8.dp))
            LazyColumn(
                state = listState,
            ) {
                items(dailyEntries, key = { it.id }) { entry ->
                    ProteinEntryItem(
                        entry,
                        onDismiss = {
                            viewModel.removeProteinEntry(entry)
                        }
                    )
                }
            }
        }
    }
}