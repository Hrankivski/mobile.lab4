package com.example.lab4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab4.components.InputField
import com.example.lab4.components.ResultCard
import com.example.lab4.components.ExampleButton
import com.example.lab4.logic.Formulas
import com.example.lab4.logic.Examples
import com.example.lab4.utils.Validation

@Composable
fun Task2Screen(onBack: () -> Unit) {
    var uLine by remember { mutableStateOf("10.0") } // кВ
    var zSigma by remember { mutableStateOf("0.45") } // Ом
    var z1 by remember { mutableStateOf("0.15") }
    var z2 by remember { mutableStateOf("0.15") }
    var z0 by remember { mutableStateOf("0.30") }
    var resultText by remember { mutableStateOf("") }

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Розрахунок струмів КЗ на шинах ГПП",
            style = MaterialTheme.typography.titleLarge
        )

        Divider()

        Text("Вхідні дані", style = MaterialTheme.typography.titleMedium)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("Напруга лінії (кВ)", uLine, { uLine = it }, Modifier.weight(1f))
                InputField("Повний опір ZΣ (Ом)", zSigma, { zSigma = it }, Modifier.weight(1f))
            }

            Text("Параметри для однофазного розрахунку", style = MaterialTheme.typography.titleSmall)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("Z1 (Ом)", z1, { z1 = it }, Modifier.weight(1f))
                InputField("Z2 (Ом)", z2, { z2 = it }, Modifier.weight(1f))
                InputField("Z0 (Ом)", z0, { z0 = it }, Modifier.weight(1f))
            }
        }

        Divider()

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            ExampleButton("Підставити приклад 7.2") {
                val ex = Examples.example7_2()
                uLine = ex.uLine.toString()
                zSigma = ex.zSigma.toString()
                z1 = ex.z1.toString()
                z2 = ex.z2.toString()
                z0 = ex.z0.toString()
            }

            Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Назад")
            }
        }

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
                    appendLine("Результати розрахунку струмів короткого замикання:")
                    appendLine("Трифазний струм КЗ: I₃ф = ${"%.2f".format(i3)} A")
                    appendLine("Однофазний струм КЗ: I₁ф = ${"%.2f".format(i1)} A")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Розрахувати струми КЗ")
        }

        ResultCard(
            "Результат",
            listOf(
                if (resultText.isEmpty())
                    "Натисніть «Розрахувати струми КЗ» для відображення результату."
                else
                    resultText
            )
        )
    }
}
