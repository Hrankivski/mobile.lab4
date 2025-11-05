package com.example.lab4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab4.components.NumberField
import com.example.lab4.components.ResultCard
import com.example.lab4.logic.FaultFormulas
import com.example.lab4.utils.Validation

@Composable
fun CalculatorScreen() {
    var Ukn by remember { mutableStateOf("6.0") }
    var Ztot by remember { mutableStateOf("0.5") }
    var Z1 by remember { mutableStateOf("0.2") }
    var Z2 by remember { mutableStateOf("0.2") }
    var Z0 by remember { mutableStateOf("0.2") }
    var tsec by remember { mutableStateOf("1.0") }
    var S by remember { mutableStateOf("50.0") }

    var selectedMaterial by remember { mutableStateOf("Мідь") }
    val materials = listOf("Мідь", "Алюміній")

    var faultType by remember { mutableStateOf("Трифазне КЗ") }
    val faultTypes = listOf("Трифазне КЗ", "Однофазне КЗ")

    var resultLines by remember { mutableStateOf(listOf<String>()) }
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Розрахунок струмів короткого замикання", style = MaterialTheme.typography.titleLarge)

        NumberField("Номінальна напруга, кВ", Ukn, { Ukn = it })

        Divider()
        Text("Тип короткого замикання", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            faultTypes.forEach { type ->
                Row(Modifier.weight(1f)) {
                    RadioButton(
                        selected = faultType == type,
                        onClick = { faultType = type }
                    )
                    Text(type, Modifier.padding(start = 4.dp))
                }
            }
        }

        NumberField("Повний опір у точці КЗ (ZΣ), Ом", Ztot, { Ztot = it })

        Divider()
        if (faultType == "Однофазне КЗ") {
            Text("Параметри для однофазного КЗ", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                NumberField("Z1 (Ом)", Z1, { Z1 = it }, Modifier.weight(1f))
                NumberField("Z2 (Ом)", Z2, { Z2 = it }, Modifier.weight(1f))
                NumberField("Z0 (Ом)", Z0, { Z0 = it }, Modifier.weight(1f))
            }
        }

        Divider()
        Text("Параметри провідника", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Час КЗ t (с)", tsec, { tsec = it }, Modifier.weight(1f))
            NumberField("Переріз S (мм²)", S, { S = it }, Modifier.weight(1f))
        }

        Text("Матеріал провідника", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            materials.forEach { material ->
                Row(Modifier.weight(1f)) {
                    RadioButton(
                        selected = selectedMaterial == material,
                        onClick = { selectedMaterial = material }
                    )
                    Text(material, Modifier.padding(start = 4.dp))
                }
            }
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                Ukn = "6.0"; Ztot = "0.45"
                Z1 = "0.15"; Z2 = "0.15"; Z0 = "0.30"
                tsec = "1.0"; S = "95.0"
                selectedMaterial = "Мідь"
                faultType = "Трифазне КЗ"
            }, Modifier.weight(1f)) {
                Text("Підставити приклад")
            }

            Button(onClick = {
                val U = Validation.toDoubleOrZero(Ukn)
                val Zt = Validation.toDoubleOrZero(Ztot)
                val z1 = Validation.toDoubleOrZero(Z1)
                val z2 = Validation.toDoubleOrZero(Z2)
                val z0 = Validation.toDoubleOrZero(Z0)
                val t = Validation.toDoubleOrZero(tsec)
                var s = Validation.toDoubleOrZero(S)

                val (k, kd) = FaultFormulas.getMaterialCoefficients(selectedMaterial)

                val Icalc = if (faultType == "Трифазне КЗ")
                    FaultFormulas.threePhaseFaultCurrent(U, Zt)
                else
                    FaultFormulas.singlePhaseFaultCurrent(U, z1, z2, z0)

                val (IthermAdm, thermOk) = FaultFormulas.checkThermal(Icalc, s, t, k)
                val (IdynAdm, dynOk) = FaultFormulas.checkDynamic(Icalc, s, kd)

                val lines = mutableListOf<String>()
                lines.add("Тип короткого замикання: $faultType")
                lines.add("Iкз = ${"%.2f".format(Icalc)} А")
                lines.add("")
                lines.add("Перевірка термічної стійкості:")
                lines.add("Допустимий струм = ${"%.2f".format(IthermAdm)} А → ${if (thermOk) "OK" else "НЕ OK"}")
                lines.add("")
                lines.add("Перевірка динамічної стійкості:")
                lines.add("Допустимий струм = ${"%.2f".format(IdynAdm)} А → ${if (dynOk) "OK" else "НЕ OK"}")

                if (!thermOk || !dynOk) {
                    lines.add("")
                    if (selectedMaterial == "Алюміній") {
                        lines.add("Рекомендація: обрати мідний провідник або збільшити переріз.")
                    } else {
                        val recommendedS = FaultFormulas.recommendBetterSection(Icalc, s, t, k, kd)
                        lines.add("Рекомендація: збільшити переріз до ≈ ${"%.0f".format(recommendedS)} мм².")
                    }
                }

                resultLines = lines
            }, Modifier.weight(1f)) {
                Text("Розрахувати")
            }
        }

        if (resultLines.isEmpty()) {
            ResultCard("Результати", listOf("Натисніть «Розрахувати» для отримання результатів"))
        } else {
            ResultCard("Результати розрахунку", resultLines)
        }
    }
}
