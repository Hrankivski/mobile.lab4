package com.example.lab4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab4.components.ExampleButton
import com.example.lab4.components.InputField
import com.example.lab4.components.ResultCard
import com.example.lab4.logic.Examples
import com.example.lab4.logic.Formulas
import com.example.lab4.utils.Validation

@Composable
fun Task3Screen(onBack: () -> Unit) {
    var selectedMode by remember { mutableStateOf("Нормальний") }
    val modes = listOf("Нормальний", "Мінімальний", "Аварійний")

    var uLine by remember { mutableStateOf("10.0") }
    var zSigma by remember { mutableStateOf("0.45") }
    var z1 by remember { mutableStateOf("0.15") }
    var z2 by remember { mutableStateOf("0.15") }
    var z0 by remember { mutableStateOf("0.30") }

    var resultText by remember { mutableStateOf("") }
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("ХПнЕМ (режими)", style = MaterialTheme.typography.titleLarge)

        Text("Режим роботи підстанції", style = MaterialTheme.typography.titleMedium)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            modes.forEach { mode ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = mode }
                    )
                    Text(mode, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Divider()

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InputField("Напруга (кВ)", uLine, { uLine = it })
            InputField("ZΣ (Ом)", zSigma, { zSigma = it })
        }

        Text("Параметри для однофазного КЗ", style = MaterialTheme.typography.titleMedium)
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InputField("Z1 (Ом)", z1, { z1 = it })
            InputField("Z2 (Ом)", z2, { z2 = it })
            InputField("Z0 (Ом)", z0, { z0 = it })
        }

        Divider()

        ExampleButton("Підставити приклад 7.4") {
            val ex = Examples.example7_4()
            selectedMode = ex.mode
            uLine = ex.uLine.toString()
            zSigma = ex.zSigma.toString()
            z1 = ex.z1.toString()
            z2 = ex.z2.toString()
            z0 = ex.z0.toString()
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val u = Validation.toDoubleOrZero(uLine)
                    val zs = Validation.toDoubleOrZero(zSigma)
                    val z1v = Validation.toDoubleOrZero(z1)
                    val z2v = Validation.toDoubleOrZero(z2)
                    val z0v = Validation.toDoubleOrZero(z0)

                    val i3 = Formulas.threePhaseFaultCurrent(u, zs)
                    val i1 = Formulas.singlePhaseFaultCurrent(u, z1v, z2v, z0v)

                    resultText = buildString {
                        appendLine("Режим: $selectedMode")
                        appendLine("I₃ф = ${"%.2f".format(i3)} А")
                        appendLine("I₁ф = ${"%.2f".format(i1)} А")
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Розрахувати")
            }

            Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Назад")
            }
        }

        ResultCard(
            "Результат",
            listOf(if (resultText.isEmpty()) "Натисніть «Розрахувати»" else resultText)
        )
    }
}
