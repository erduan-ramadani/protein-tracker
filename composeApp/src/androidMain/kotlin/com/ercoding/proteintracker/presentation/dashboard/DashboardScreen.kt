package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen() {
    MaterialTheme {

        val viewModel: DashboardViewModel = koinViewModel()

        val localFocusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }

        val mealAmount = viewModel.mealAmount
        val dailyReached = viewModel.dailyReached
        val dailyGoal = viewModel.dailyGoal
        val progress = viewModel.progress
        val dailyEntries = viewModel.proteinEntries

        var userTextInput by remember { mutableStateOf("") }
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
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ProteinTracker",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.padding(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                    progress = { progress }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Proteine per Mahlzeit: $mealAmount", color = Color.Black)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Tageswert: $dailyReached / $dailyGoal", color = Color.Black)
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = userTextInput,
                    onValueChange = { userTextInput = it },
                    label = { Text("Was hast du heute gegessen?") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.addProteins(userTextInput)
                            focusRequester.freeFocus()
                            keyboardController?.hide()
                        }
                    )
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = {
                        viewModel.addProteins(userTextInput)
                        localFocusManager.clearFocus()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Proteine hinzufügen")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {
                    items(dailyEntries) { entry ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    entry.meal,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    entry.proteinAmount.toString() + "g",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}