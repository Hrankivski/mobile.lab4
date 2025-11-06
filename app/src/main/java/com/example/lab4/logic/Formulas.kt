package com.example.lab4.logic

import kotlin.math.sqrt

object Formulas {
    // Трифазний струм КЗ
    fun threePhaseFaultCurrent(U_kV: Double, Z_total: Double): Double {
        if (Z_total == 0.0) return 0.0
        val U_v = U_kV * 1000.0
        return U_v / (sqrt(3.0) * Z_total)
    }

    // Однофазний струм КЗ
    fun singlePhaseFaultCurrent(U_kV: Double, Z1: Double, Z2: Double, Z0: Double): Double {
        val denom = Z1 + Z2 + Z0
        if (denom == 0.0) return 0.0
        val U_v = U_kV * 1000.0
        return U_v / (3.0 * denom)
    }

    // Перевірка термічної стійкості
    fun checkThermal(I: Double, S: Double, t: Double, k: Double): Pair<Double, Boolean> {
        val Iadm = if (t <= 0.0) 0.0 else k * S / sqrt(t)
        return Iadm to (I <= Iadm)
    }

    // Перевірка динамічної стійкості
    fun checkDynamic(I: Double, S: Double, kd: Double): Pair<Double, Boolean> {
        val Iadm = kd * S
        return Iadm to (I <= Iadm)
    }

    // Падіння напруги (В)
    fun voltageDrop(I: Double, length: Double, resistivity: Double, section: Double): Double {
        if (section == 0.0) return Double.POSITIVE_INFINITY
        return 2.0 * I * length * resistivity / section
    }

    // Підбір перерізу
    private val standardSections = listOf(1.5, 2.5, 4.0, 6.0, 10.0, 16.0, 25.0, 35.0, 50.0, 70.0, 95.0)

    // Типові константи для матеріалів
    private val materialProps = mapOf(
        "мідь" to Triple(0.0175, 115.0, 8.0),       // rho, k, kd
        "мідь_uc" to Triple(0.0175, 115.0, 8.0),
        "мі́дь" to Triple(0.0175, 115.0, 8.0),
        "алюміній" to Triple(0.0282, 76.0, 6.0),
        "Алюміній" to Triple(0.0282, 76.0, 6.0),
        "Мідь" to Triple(0.0175, 115.0, 8.0)
    )

    fun suggestCableForLoad(
        loadKW: Double,
        uLine_kV: Double,
        powerFactor: Double,
        length: Double,
        material: String,
        insulation: String,
        allowedDropPercent: Double = 3.0,
        tShort: Double = 1.0,
        loadCurrentA: Double? = null,
        currentSection: Double = 0.0
    ): String {
        val matKey = if (material.isBlank()) "Мідь" else material
        val props = materialProps[matKey] ?: materialProps["Мідь"]!!
        val rho = props.first
        val k = props.second
        val kd = props.third

        val Iload = loadCurrentA ?: if (uLine_kV == 0.0) 0.0 else (loadKW * 1000.0) / (sqrt(3.0) * uLine_kV * 1000.0 * powerFactor) * 1000.0 / 1000.0
        val I = if (uLine_kV == 0.0) 0.0 else loadKW / (sqrt(3.0) * uLine_kV) / powerFactor

        val maxDropV = uLine_kV * 1000.0 * (allowedDropPercent / 100.0)

        val sb = StringBuilder()
        sb.appendLine("Вхідні: P=${"%.2f".format(loadKW)} кВт, U=${"%.2f".format(uLine_kV)} кВ, L=${"%.1f".format(length)} м")
        sb.appendLine("Матеріал: $material, Ізоляція: $insulation")
        sb.appendLine("Оцінка струму навантаження I ≈ ${"%.2f".format(I)} A")
        sb.appendLine()

        if (currentSection > 0.0) {
            val dropV = voltageDrop(I, length, rho, currentSection)
            val (Ith, okTherm) = checkThermal(I, currentSection, tShort, k)
            val (Idyn, okDyn) = checkDynamic(I, currentSection, kd)
            sb.appendLine("Поточний переріз S=${"%.1f".format(currentSection)} мм²:")
            sb.appendLine("  Допустимий термічний струм I_терм=${"%.2f".format(Ith)} A → ${if (okTherm) "OK" else "НЕ OK"}")
            sb.appendLine("  Допустимий динамічний струм I_дин=${"%.2f".format(Idyn)} A → ${if (okDyn) "OK" else "НЕ OK"}")
            sb.appendLine("  Падіння напруги ΔU=${"%.2f".format(dropV)} В (${String.format("%.2f", dropV / (uLine_kV*1000.0)*100)}%) → ${if (dropV <= maxDropV) "OK" else "НЕ OK"}")
            if (okTherm && okDyn && dropV <= maxDropV) {
                sb.appendLine()
                sb.appendLine("Результат: переріз відповідає вимогам.")
                return sb.toString()
            } else {
                sb.appendLine()
                sb.appendLine("Поточний переріз НЕ відповідає вимогам — шукаємо відповідний...")
            }
        }

        val candidate = standardSections.firstOrNull { s ->
            val (Ith, okTh) = checkThermal(I, s, tShort, k)
            val (Id, okD) = checkDynamic(I, s, kd)
            val dropV = voltageDrop(I, length, rho, s)
            okTh && okD && dropV <= maxDropV
        }

        if (candidate != null) {
            val (Ith, _) = checkThermal(I, candidate, tShort, k)
            val (Id, _) = checkDynamic(I, candidate, kd)
            val dropV = voltageDrop(I, length, rho, candidate)
            sb.appendLine("Рекомендовано переріз S = ${"%.1f".format(candidate)} мм²")
            sb.appendLine("  Допустимий термічний струм I_терм=${"%.2f".format(Ith)} A")
            sb.appendLine("  Допустимий динамічний струм I_дин=${"%.2f".format(Id)} A")
            sb.appendLine("  Падіння напруги ΔU=${"%.2f".format(dropV)} В (${String.format("%.2f", dropV / (uLine_kV*1000.0)*100)}%)")
            return sb.toString()
        }

        sb.appendLine("Не знайдено стандартного перерізу, що задовольняє обидві умови.")
        sb.appendLine("Рекомендація: зменшити довжину кабелю, збільшити кількість паралельних жил або змінити матеріал на мідь.")
        return sb.toString()
    }
}
