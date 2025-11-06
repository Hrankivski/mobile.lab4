package com.example.lab4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab4.components.*
import com.example.lab4.logic.*
import com.example.lab4.utils.Validation

@Composable
fun Task1Screen(onBack: () -> Unit) {
    var material by remember { mutableStateOf("Мідь") }
    var insulation by remember { mutableStateOf("ПВХ") }
    val materials = listOf("Мідь", "Алюміній")
    val insulations = listOf("ПВХ", "XLPE")

    var loadKW by remember { mutableStateOf("") }
    var uLine by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var allowedDropPct by remember { mutableStateOf("") }
    var powerFactor by remember { mutableStateOf("") }
    var tShort by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("") }

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
            "Підбір кабелів",
            style = MaterialTheme.typography.titleLarge
        )

        Text("Характеристики кабелю", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownSelector("Матеріал", material, materials) { material = it }
            DropdownSelector("Ізоляція", insulation, insulations) { insulation = it }
        }

        Divider()

        Text("Вхідні параметри", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("Навантаження (кВт)", loadKW, { loadKW = it }, Modifier.weight(1f))
                InputField("Напруга (кВ)", uLine, { uLine = it }, Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("Довжина кабелю (м)", length, { length = it }, Modifier.weight(1f))
                InputField("Доп. падіння (%)", allowedDropPct, { allowedDropPct = it }, Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("cosφ", powerFactor, { powerFactor = it }, Modifier.weight(1f))
                InputField("Час КЗ t (с)", tShort, { tShort = it }, Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                InputField("Переріз (мм²)", section, { section = it }, Modifier.weight(1f))
                ExampleButton("Підставити приклад 7.1") {
                    val ex = Examples.example7_1()
                    material = ex.material
                    insulation = ex.insulation
                    loadKW = ex.loadKW.toString()
                    uLine = ex.uLine.toString()
                    length = ex.length.toString()
                    allowedDropPct = ex.allowedDropPct.toString()
                    powerFactor = ex.powerFactor.toString()
                    tShort = ex.tShort.toString()
                    section = ex.section.toString()
                }
            }
        }

        Divider()

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val load = Validation.toDoubleOrZero(loadKW)
                    val u = Validation.toDoubleOrZero(uLine)
                    val L = Validation.toDoubleOrZero(length)
                    val drop = Validation.toDoubleOrZero(allowedDropPct)
                    val pf = Validation.toDoubleOrZero(powerFactor)
                    val t = Validation.toDoubleOrZero(tShort)
                    val s = Validation.toDoubleOrZero(section)

                    val result = Formulas.suggestCableForLoad(
                        loadKW = load,
                        uLine_kV = u,
                        powerFactor = pf,
                        length = L,
                        material = material,
                        insulation = insulation,
                        allowedDropPercent = drop,
                        tShort = t,
                        currentSection = s
                    )
                    resultText = result
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Порахувати та підібрати")
            }

            Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Назад")
            }
        }

        ResultCard(
            "Результат",
            listOf(
                if (resultText.isEmpty())
                    "Натисніть «Порахувати та підібрати»"
                else
                    resultText
            )
        )
    }
}
