package com.watchclock.tracker.util

/**
 * Provides fuzzy matching for autocomplete suggestions using Levenshtein distance.
 */
object AutocompleteHelper {

    /** Returns suggestions from [candidates] that best match [query] using fuzzy matching. */
    fun getSuggestions(
        query: String,
        candidates: List<String>,
        maxResults: Int = 10,
        maxDistance: Int = 3
    ): List<String> {
        if (query.isBlank()) return candidates.take(maxResults)

        val q = query.lowercase()

        return candidates
            .filter { it.isNotBlank() }
            .map { candidate ->
                val lower = candidate.lowercase()
                val score = when {
                    lower == q -> 0
                    lower.startsWith(q) -> 1
                    lower.contains(q) -> 2
                    else -> levenshteinDistance(q, lower)
                }
                candidate to score
            }
            .filter { (_, score) -> score <= maxDistance || score <= 2 }
            .sortedBy { (_, score) -> score }
            .map { (candidate, _) -> candidate }
            .take(maxResults)
    }

    /** Calculates the Levenshtein edit distance between two strings. */
    fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }
        return dp[m][n]
    }
}
