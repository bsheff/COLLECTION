package com.watchclock.tracker.util

/**
 * Provides spell-check suggestions by matching input against known brand names
 * and their recorded misspellings.
 */
object SpellCheckHelper {

    data class SpellCheckResult(
        val corrected: String?,
        val confidence: Float,
        val suggestions: List<String>
    )

    /**
     * Checks [input] against [knownNames] and their [misspellings] map.
     * Returns correction suggestions when no exact match is found.
     */
    fun check(
        input: String,
        knownNames: List<String>,
        misspellings: Map<String, List<String>> = emptyMap()
    ): SpellCheckResult {
        val trimmed = input.trim()
        val lower = trimmed.lowercase()

        // Exact match
        if (knownNames.any { it.equals(trimmed, ignoreCase = true) }) {
            return SpellCheckResult(null, 1f, emptyList())
        }

        // Check misspellings map
        for ((correct, wrongs) in misspellings) {
            if (wrongs.any { it.equals(trimmed, ignoreCase = true) }) {
                return SpellCheckResult(correct, 0.95f, listOf(correct))
            }
        }

        // Fuzzy match
        val ranked = knownNames.map { name ->
            val dist = AutocompleteHelper.levenshteinDistance(lower, name.lowercase())
            name to dist
        }.sortedBy { it.second }

        val best = ranked.firstOrNull()
        return if (best != null && best.second <= 3) {
            val suggestions = ranked.filter { it.second <= 3 }.map { it.first }.take(5)
            val confidence = 1f - (best.second.toFloat() / maxOf(lower.length, 1))
            SpellCheckResult(best.first, confidence, suggestions)
        } else {
            SpellCheckResult(null, 0f, emptyList())
        }
    }
}
