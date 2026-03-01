package com.watchclock.tracker.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.ui.components.ItemCard
import com.watchclock.tracker.ui.components.StatCard
import com.watchclock.tracker.ui.viewmodel.HomeViewModel
import com.watchclock.tracker.util.CurrencyHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues,
    onNavigateToWatches: () -> Unit,
    onNavigateToClocks: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: (String) -> Unit
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val recentItems by viewModel.recentItems.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Collection",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            Column {
                SmallFloatingActionButton(
                    onClick = { onNavigateToAdd("CLOCK") },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Filled.Schedule, contentDescription = "Add Clock")
                }
                Spacer(Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = { onNavigateToAdd("WATCH") }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Watch")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = paddingValues.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Overview",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Watches",
                        value = stats.watchCount.toString(),
                        icon = Icons.Filled.Watch,
                        modifier = Modifier.weight(1f),
                        subtitle = CurrencyHelper.formatCompact(stats.totalWatchValue)
                    )
                    StatCard(
                        title = "Clocks",
                        value = stats.clockCount.toString(),
                        icon = Icons.Filled.Schedule,
                        modifier = Modifier.weight(1f),
                        subtitle = CurrencyHelper.formatCompact(stats.totalClockValue),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val totalValue = (stats.totalWatchValue ?: 0.0) + (stats.totalClockValue ?: 0.0)
                    val totalCost = (stats.totalWatchCost ?: 0.0) + (stats.totalClockCost ?: 0.0)
                    StatCard(
                        title = "Total Value",
                        value = CurrencyHelper.formatCompact(totalValue.takeIf { it > 0 }),
                        icon = Icons.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Total Cost",
                        value = CurrencyHelper.formatCompact(totalCost.takeIf { it > 0 }),
                        icon = Icons.Filled.ShoppingCart,
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Items", style = MaterialTheme.typography.titleLarge)
                    Row {
                        TextButton(onClick = onNavigateToWatches) { Text("Watches") }
                        TextButton(onClick = onNavigateToClocks) { Text("Clocks") }
                    }
                }
            }

            if (recentItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.WatchOff,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No items yet — tap + to add your first piece",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(recentItems, key = { it.id }) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onNavigateToDetail(item.id) }
                    )
                }
            }
        }
    }
}
