package com.ercoding.proteintracker.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {

    val viewModel: SettingsViewModel = koinViewModel()
    val isDarkMode by viewModel.isDarkMode.collectAsState(false)
    val dailyGoal = viewModel.proteinGoal.collectAsState(null).value?.toString() ?: ""
    var newDailyGoal by remember { mutableStateOf(dailyGoal) }

    LaunchedEffect(dailyGoal) {
        if (newDailyGoal.isEmpty()) newDailyGoal = dailyGoal
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Einstellungen")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "arrowBack")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListItem(
                headlineContent = { Text("Dark Mode") },
                supportingContent = { Text("App im Dunkelmodus anzeigen") },
                trailingContent = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() }
                    )
                }
            )
            ListItem(
                headlineContent = { Text("Proteinziel") },
                supportingContent = { Text("Täglicher Eiweißbedarf") },
                trailingContent = {
                    Text(text = "$dailyGoal g", style = MaterialTheme.typography.bodyLarge)
                }
            )
            OutlinedTextField(
                value = newDailyGoal,
                placeholder = { Text(dailyGoal) },
                onValueChange = { newDailyGoal = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) newDailyGoal = ""
                    }
            )
            Button(
                onClick = {
                    viewModel.setProteinGoal(newDailyGoal.toInt())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Speichern")
            }
        }
    }
}