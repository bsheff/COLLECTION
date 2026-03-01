package com.watchclock.tracker.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyHelper {

    private val defaultFormatter: NumberFormat
        get() = NumberFormat.getCurrencyInstance(Locale.getDefault())

    fun format(amount: Double?, currencyCode: String = "USD"): String {
        if (amount == null) return "—"
        return try {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance(currencyCode)
            formatter.format(amount)
        } catch (e: Exception) {
            "$${String.format("%.2f", amount)}"
        }
    }

    fun formatCompact(amount: Double?): String {
        if (amount == null) return "—"
        return when {
            amount >= 1_000_000 -> "$${String.format("%.1f", amount / 1_000_000)}M"
            amount >= 1_000 -> "$${String.format("%.1f", amount / 1_000)}K"
            else -> "$${String.format("%.2f", amount)}"
        }
    }

    fun parse(input: String): Double? {
        return try {
            val cleaned = input.replace("[^0-9.]".toRegex(), "")
            cleaned.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
