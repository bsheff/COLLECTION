package com.watchclock.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.watchclock.tracker.data.model.Feature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureChips(
    selectedFeatures: Set<Feature>,
    onFeatureToggle: (Feature) -> Unit,
    modifier: Modifier = Modifier,
    availableFeatures: List<Feature> = Feature.entries
) {
    Column(modifier = modifier) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        // Group features into rows for better layout
        val chunked = availableFeatures.chunked(3)
        chunked.forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowFeatures.forEach { feature ->
                    val selected = feature in selectedFeatures
                    FilterChip(
                        selected = selected,
                        onClick = { onFeatureToggle(feature) },
                        label = { Text(feature.displayName, style = MaterialTheme.typography.bodySmall) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining columns if row is incomplete
                repeat(3 - rowFeatures.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}
