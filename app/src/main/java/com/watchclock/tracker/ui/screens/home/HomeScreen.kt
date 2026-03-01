package com.watchclock.tracker.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.watchclock.tracker.ui.components.icon
import com.watchclock.tracker.ui.viewmodel.HomeViewModel
import com.watchclock.tracker.util.CurrencyHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues,
    onNavigateToCollections: (CollectionType) -> Unit,
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
            FloatingActionButton(
                onClick = { onNavigateToAdd(CollectionType.WATCH.name) }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
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
                    val totalCount = stats.countByType.values.sum()
                    StatCard(
                        title = "Total Items",
                        value = totalCount.toString(),
                        icon = Icons.Filled.Inventory2,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Total Value",
                        value = CurrencyHelper.formatCompact(stats.totalValue),
                        icon = Icons.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Cost",
                        value = CurrencyHelper.formatCompact(stats.totalCost),
                        icon = Icons.Filled.ShoppingCart,
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                    StatCard(
                        title = "Collections",
                        value = stats.countByType.size.toString(),
                        icon = Icons.Filled.CollectionsBookmark,
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

            if (stats.countByType.isNotEmpty()) {
                item {
                    Text(
                        "By Collection",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(stats.countByType.entries.toList()) { (type, count) ->
                            StatCard(
                                title = type.displayName,
                                value = count.toString(),
                                icon = type.icon(),
                                modifier = Modifier.width(140.dp),
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                onClick = { onNavigateToCollections(type) }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Items", style = MaterialTheme.typography.titleLarge)
                    TextButton(onClick = { onNavigateToCollections(CollectionType.WATCH) }) {
                        Text("View All")
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
                                Icons.Filled.Inventory2,
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
