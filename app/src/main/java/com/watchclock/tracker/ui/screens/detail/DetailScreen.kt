package com.watchclock.tracker.ui.screens.detail

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
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.Feature
import com.watchclock.tracker.data.model.ServiceHistory
import com.watchclock.tracker.ui.components.PhotoGallery
import com.watchclock.tracker.ui.viewmodel.DetailViewModel
import com.watchclock.tracker.util.CurrencyHelper
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long, String) -> Unit
) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    val photos by viewModel.photos.collectAsStateWithLifecycle()
    val serviceHistory by viewModel.serviceHistory.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddServiceDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete this item? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteItem { onNavigateBack() }
                        showDeleteDialog = false
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddServiceDialog) {
        AddServiceDialog(
            itemId = item?.id ?: 0L,
            onDismiss = { showAddServiceDialog = false },
            onSave = { service ->
                viewModel.addServiceRecord(service)
                showAddServiceDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.let { "${it.brand} ${it.model}" } ?: "Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    item?.let { i ->
                        IconButton(onClick = { onNavigateToEdit(i.id, i.type.name) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (item == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val i = item!!

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photos
            item {
                PhotoGallery(
                    photos = photos,
                    onDeletePhoto = { photo -> viewModel.deletePhoto(photo) }
                )
            }

            // Basic info
            item { SectionHeader("Basic Information") }
            item { DetailInfoCard(i) }

            // Condition
            item { SectionHeader("Condition") }
            item { ConditionCard(i) }

            // Values
            if (i.purchaseCost != null || i.currentValue != null || i.insuranceValue != null) {
                item { SectionHeader("Valuation") }
                item { ValuationCard(i) }
            }

            // Features
            val features = remember(i.features) {
                try {
                    Json.decodeFromString<List<String>>(i.features)
                        .mapNotNull { runCatching { Feature.valueOf(it) }.getOrNull() }
                } catch (_: Exception) { emptyList() }
            }
            if (features.isNotEmpty()) {
                item { SectionHeader("Features") }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        features.forEach { feature ->
                            AssistChip(
                                onClick = {},
                                label = { Text(feature.displayName) }
                            )
                        }
                    }
                }
            }

            // Notes & Provenance
            if (!i.notes.isNullOrBlank() || !i.provenance.isNullOrBlank() || !i.repairsNeeded.isNullOrBlank()) {
                item { SectionHeader("Notes") }
                item { NotesCard(i) }
            }

            // Service history
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Service History",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showAddServiceDialog = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add service record")
                    }
                }
            }

            if (serviceHistory.isEmpty()) {
                item {
                    Text(
                        "No service records yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(serviceHistory) { record ->
                    ServiceRecordCard(
                        record = record,
                        onDelete = { viewModel.deleteServiceRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun DetailInfoCard(item: CollectionItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            DetailRow("Type", item.type.name.lowercase().replaceFirstChar { it.uppercase() })
            DetailRow("Brand", item.brand)
            DetailRow("Model", item.model)
            if (item.serialNumber.isNotBlank()) DetailRow("Serial #", item.serialNumber)
            item.yearOfManufacture?.let { DetailRow("Year", it.toString()) }
            item.era?.let { DetailRow("Era", it) }
            item.category?.let { DetailRow("Category", it) }
            item.movementType?.let { DetailRow("Movement", it) }
            item.location?.let { DetailRow("Location", it) }
            item.tags?.let { DetailRow("Tags", it) }
        }
    }
}

@Composable
private fun ConditionCard(item: CollectionItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item.cosmeticCondition?.let { DetailRow("Cosmetic", it) }
            item.mechanicalCondition?.let { DetailRow("Mechanical", it) }
            item.repairsNeeded?.let { DetailRow("Repairs Needed", it) }
        }
    }
}

@Composable
private fun ValuationCard(item: CollectionItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item.purchaseDate?.let { DetailRow("Purchase Date", it) }
            item.purchaseCost?.let { DetailRow("Purchase Cost", CurrencyHelper.format(it)) }
            item.currentValue?.let { DetailRow("Current Value", CurrencyHelper.format(it)) }
            item.insuranceValue?.let { DetailRow("Insurance Value", CurrencyHelper.format(it)) }
        }
    }
}

@Composable
private fun NotesCard(item: CollectionItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item.notes?.let { DetailRow("Notes", it) }
            item.provenance?.let { DetailRow("Provenance", it) }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(130.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ServiceRecordCard(
    record: ServiceHistory,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(record.serviceType, style = MaterialTheme.typography.titleSmall)
                Text(record.serviceDate, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                record.servicedBy?.let {
                    Text("By: $it", style = MaterialTheme.typography.bodySmall)
                }
                record.cost?.let {
                    Text("Cost: ${CurrencyHelper.format(it)}", style = MaterialTheme.typography.bodySmall)
                }
                record.notes?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
                record.nextServiceDue?.let {
                    Text("Next due: $it", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete record",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddServiceDialog(
    itemId: Long,
    onDismiss: () -> Unit,
    onSave: (ServiceHistory) -> Unit
) {
    var serviceType by remember { mutableStateOf("") }
    var serviceDate by remember { mutableStateOf("") }
    var servicedBy by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var nextDue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Service Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = serviceType,
                    onValueChange = { serviceType = it },
                    label = { Text("Service Type*") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = serviceDate,
                    onValueChange = { serviceDate = it },
                    label = { Text("Date (YYYY-MM-DD)*") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = servicedBy,
                    onValueChange = { servicedBy = it },
                    label = { Text("Serviced By") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Cost") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nextDue,
                    onValueChange = { nextDue = it },
                    label = { Text("Next Service Due") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (serviceType.isNotBlank() && serviceDate.isNotBlank()) {
                        onSave(
                            ServiceHistory(
                                itemId = itemId,
                                serviceType = serviceType,
                                serviceDate = serviceDate,
                                servicedBy = servicedBy.ifBlank { null },
                                cost = cost.toDoubleOrNull(),
                                notes = notes.ifBlank { null },
                                nextServiceDue = nextDue.ifBlank { null }
                            )
                        )
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
