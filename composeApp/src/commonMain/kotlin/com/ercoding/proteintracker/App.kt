package com.ercoding.proteintracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {

        var userTextInput by remember { mutableStateOf("") }
        var consumedProtein by remember { mutableStateOf(0.75f) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ProteinTracker", color = Color.Black)
            Spacer(modifier = Modifier.padding(8.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                progress = { consumedProtein }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedTextField(
                value = userTextInput,
                onValueChange = { userTextInput = it },
                label = { Text("Was hast du heute gegessen?") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {},
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Proteine hinzufügen")
            }
        }
    }
}