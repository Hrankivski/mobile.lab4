package com.example.lab4.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ExampleButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) { Text(text) }
}
