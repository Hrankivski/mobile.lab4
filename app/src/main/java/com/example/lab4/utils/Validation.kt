package com.example.lab4.utils

object Validation {
    fun toDoubleOrZero(value: String): Double {
        return value.replace(',', '.').toDoubleOrNull() ?: 0.0
    }
}
