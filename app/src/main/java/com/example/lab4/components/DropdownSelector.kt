package com.example.lab4.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownSelector(label: String, selected: String, options: List<String>, onSelect: (String) -> Unit) {
    Row {
        Text("$label:", modifier = Modifier.padding(end = 8.dp))
        options.forEach { opt ->
            Row(modifier = Modifier.padding(end = 8.dp)) {
                RadioButton(selected = selected == opt, onClick = { onSelect(opt) })
                Text(opt, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
