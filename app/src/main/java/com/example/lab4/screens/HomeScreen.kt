package com.example.lab4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Калькулятори: підбір кабелів, струми на шинах ГПП, ХПнЕМ (режими)", style = MaterialTheme.typography.bodyLarge)

        Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Завдання 1 — Підбір кабелів", style = MaterialTheme.typography.titleMedium)
                Text("Підібрати кабелі для двотрансформаторної підстанції. Калькулятор підбору перерізу з урахуванням ΔU і Iдоп.")
                Button(onClick = { onNavigate("task1") }, modifier = Modifier.fillMaxWidth()) { Text("Перейти") }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Завдання 2 — Струми на шинах ГПП", style = MaterialTheme.typography.titleMedium)
                Text("Розрахунок Iкз на шинах 10 кВ за заданими опорами.")
                Button(onClick = { onNavigate("task2") }, modifier = Modifier.fillMaxWidth()) { Text("Перейти") }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Завдання 3 — ХПнЕМ (режими)", style = MaterialTheme.typography.titleMedium)
                Text("Розрахунок для нормального, мінімального та аварійного режимів.")
                Button(onClick = { onNavigate("task3") }, modifier = Modifier.fillMaxWidth()) { Text("Перейти") }
            }
        }
    }
}
