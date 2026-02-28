package com.watchclock.tracker.ui.screens.brands

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
import com.watchclock.tracker.data.model.Brand
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.ui.viewmodel.BrandViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandScreen(
    viewModel: BrandViewModel,
    paddingValues: PaddingValues
) {
    val brands by viewModel.brands.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddBrandDialog(
            type = selectedType,
            onDismiss = { showAddDialog = false },
            onSave = { brand ->
                viewModel.addBrand(brand)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Brands", style = MaterialTheme.typography.headlineMedium) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                // Type tabs
                TabRow(
                    selectedTabIndex = if (selectedType == CollectionType.WATCH) 0 else 1
                ) {
                    Tab(
                        selected = selectedType == CollectionType.WATCH,
                        onClick = { viewModel.setType(CollectionType.WATCH) },
                        text = { Text("Watches") },
                        icon = { Icon(Icons.Filled.Watch, null, Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedType == CollectionType.CLOCK,
                        onClick = { viewModel.setType(CollectionType.CLOCK) },
                        text = { Text("Clocks") },
                        icon = { Icon(Icons.Filled.Schedule, null, Modifier.size(18.dp)) }
                    )
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.setSearchQuery(it)
                    },
                    placeholder = { Text("Search brands…") },
                    leadingIcon = { Icon(Icons.Filled.Search, null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                            }) { Icon(Icons.Filled.Clear, null) }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Brand")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 4.dp,
                bottom = paddingValues.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(brands, key = { it.id }) { brand ->
                BrandCard(
                    brand = brand,
                    onDelete = { viewModel.deleteBrand(brand) }
                )
            }
        }
    }
}

@Composable
private fun BrandCard(
    brand: Brand,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(brand.name, style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    brand.country?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    brand.foundedYear?.let {
                        Text("est. $it", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            if (brand.isCustom) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "Default brand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBrandDialog(
    type: CollectionType,
    onDismiss: () -> Unit,
    onSave: (Brand) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var foundedYear by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Brand") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Brand Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text("Country") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = foundedYear,
                    onValueChange = { foundedYear = it },
                    label = { Text("Founded Year") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            Brand(
                                name = name.trim(),
                                country = country.ifBlank { null },
                                foundedYear = foundedYear.toIntOrNull(),
                                type = type,
                                isCustom = true
                            )
                        )
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
