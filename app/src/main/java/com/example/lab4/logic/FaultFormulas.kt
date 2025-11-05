package com.example.lab4.logic

object FaultFormulas {

    fun threePhaseFaultCurrent(U: Double, Ztot: Double): Double {
        return if (Ztot == 0.0) 0.0 else (U * 1000) / (1.732 * Ztot)
    }

    fun singlePhaseFaultCurrent(U: Double, Z1: Double, Z2: Double, Z0: Double): Double {
        val denom = Z1 + Z2 + Z0
        return if (denom == 0.0) 0.0 else (U * 1000) / (3 * denom)
    }

    fun getMaterialCoefficients(material: String): Pair<Double, Double> {
        return when (material) {
            "Мідь" -> 115.0 to 8.0
            "Алюміній" -> 76.0 to 6.0
            else -> 100.0 to 7.0
        }
    }

    fun checkThermal(I: Double, S: Double, t: Double, k: Double): Pair<Double, Boolean> {
        val Iadm = k * S / Math.sqrt(t)
        return Iadm to (I <= Iadm)
    }

    fun checkDynamic(I: Double, S: Double, kd: Double): Pair<Double, Boolean> {
        val Iadm = kd * S
        return Iadm to (I <= Iadm)
    }

    fun recommendBetterSection(I: Double, S: Double, t: Double, k: Double, kd: Double): Double {
        var newS = S

        repeat(10000) {
            val (_, thermOk) = checkThermal(I, newS, t, k)
            val (_, dynOk) = checkDynamic(I, newS, kd)
            if (thermOk && dynOk) {
                return newS
            }
            newS *= 1.1
        }
        return newS
    }
}
