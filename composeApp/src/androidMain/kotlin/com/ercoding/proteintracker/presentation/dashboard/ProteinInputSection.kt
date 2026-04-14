package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun ProteinInputSection(
    onClick: (String) -> Unit,
    isLoading: Boolean
) {
    val localFocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var userTextInput by remember { mutableStateOf("") }


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
                onClick(userTextInput)
                focusRequester.freeFocus()
                keyboardController?.hide()
                userTextInput = ""
            }
        )
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Button(
        onClick = {
            onClick(userTextInput)
            localFocusManager.clearFocus()
            userTextInput = ""
        },
        enabled = userTextInput.isNotBlank()
                && userTextInput.length >= 3
                && userTextInput.length < 40,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text("Proteine hinzufügen")
        }
    }
}