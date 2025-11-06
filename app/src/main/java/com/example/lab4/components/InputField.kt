package com.example.lab4.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { raw -> onValueChange(raw.replace(',', '.')) },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}
