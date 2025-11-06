package com.example.lab4.logic

data class Example7_1(
    val material: String,
    val insulation: String,
    val loadKW: Double,
    val uLine: Double,
    val length: Double,
    val allowedDropPct: Double,
    val powerFactor: Double,
    val tShort: Double,
    val section: Double
)

data class Example7_2(
    val uLine: Double,
    val zSigma: Double,
    val z1: Double,
    val z2: Double,
    val z0: Double
)

data class Example7_4(
    val mode: String,
    val uLine: Double,
    val zSigma: Double,
    val z1: Double,
    val z2: Double,
    val z0: Double
)

object Examples {
    fun example7_1(): Example7_1 {
        return Example7_1(
            material = "Мідь",
            insulation = "XLPE",
            loadKW = 200.0,
            uLine = 10.0,
            length = 100.0,
            allowedDropPct = 3.0,
            powerFactor = 0.95,
            tShort = 1.0,
            section = 25.0
        )
    }

    fun example7_2(): Example7_2 {
        return Example7_2(
            uLine = 10.0,
            zSigma = 0.45,
            z1 = 0.15,
            z2 = 0.15,
            z0 = 0.30
        )
    }

    fun example7_4(): Example7_4 {
        return Example7_4(
            mode = "Нормальний",
            uLine = 10.0,
            zSigma = 0.35,
            z1 = 0.12,
            z2 = 0.14,
            z0 = 0.28
        )
    }
}
