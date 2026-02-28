package com.watchclock.tracker.ui.screens.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.model.Feature
import com.watchclock.tracker.ui.components.AutocompleteTextField
import com.watchclock.tracker.ui.components.FeatureChips
import com.watchclock.tracker.ui.viewmodel.AddEditViewModel

private val COSMETIC_CONDITIONS = listOf("Mint", "Excellent", "Very Good", "Good", "Fair", "Poor")
private val MECHANICAL_CONDITIONS = listOf(
    "Running Perfectly", "Running with Issues", "Needs Service", "Not Running", "Parts Only"
)
private val MOVEMENT_TYPES = listOf("Manual", "Automatic", "Quartz", "Electric")
private val ERAS = listOf("Victorian", "Edwardian", "Art Nouveau", "Art Deco", "Mid-Century", "Modern")
private val WATCH_CATEGORIES = listOf("Pocket Watch", "Wristwatch", "Dress Watch", "Sport Watch", "Diving Watch")
private val CLOCK_CATEGORIES = listOf("Mantel Clock", "Wall Clock", "Grandfather Clock", "Bracket Clock", "Carriage Clock", "Cuckoo Clock")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel,
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val watchBrands by viewModel.watchBrands.collectAsStateWithLifecycle()
    val clockBrands by viewModel.clockBrands.collectAsStateWithLifecycle()

    val brandSuggestions = remember(form.type, watchBrands, clockBrands) {
        (if (form.type == CollectionType.WATCH) watchBrands else clockBrands).map { it.name }
    }

    val categoryOptions = if (form.type == CollectionType.WATCH) WATCH_CATEGORIES else CLOCK_CATEGORIES

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (viewModel.form.value.brand.isEmpty()) "Add Item" else "Edit Item",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (form.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 8.dp))
                    } else {
                        IconButton(onClick = { viewModel.saveItem(onSaved) }) {
                            Icon(Icons.Filled.Check, contentDescription = "Save")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Type selector
            SectionLabel("Type")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CollectionType.entries.forEach { type ->
                    FilterChip(
                        selected = form.type == type,
                        onClick = { viewModel.update { copy(type = type) } },
                        label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            SectionLabel("Basic Information")

            AutocompleteTextField(
                value = form.brand,
                onValueChange = { viewModel.update { copy(brand = it) } },
                label = "Brand *",
                suggestions = brandSuggestions,
                modifier = Modifier.fillMaxWidth(),
                isError = form.error != null && form.brand.isBlank()
            )

            OutlinedTextField(
                value = form.model,
                onValueChange = { viewModel.update { copy(model = it) } },
                label = { Text("Model *") },
                singleLine = true,
                isError = form.error != null && form.model.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.serialNumber,
                onValueChange = { viewModel.update { copy(serialNumber = it) } },
                label = { Text("Serial Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.yearOfManufacture,
                onValueChange = { viewModel.update { copy(yearOfManufacture = it) } },
                label = { Text("Year of Manufacture") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            AutocompleteTextField(
                value = form.era,
                onValueChange = { viewModel.update { copy(era = it) } },
                label = "Era",
                suggestions = ERAS,
                modifier = Modifier.fillMaxWidth()
            )

            AutocompleteTextField(
                value = form.category,
                onValueChange = { viewModel.update { copy(category = it) } },
                label = "Category",
                suggestions = categoryOptions,
                modifier = Modifier.fillMaxWidth()
            )

            AutocompleteTextField(
                value = form.movementType,
                onValueChange = { viewModel.update { copy(movementType = it) } },
                label = "Movement Type",
                suggestions = MOVEMENT_TYPES,
                modifier = Modifier.fillMaxWidth()
            )

            SectionLabel("Condition")

            AutocompleteTextField(
                value = form.cosmeticCondition,
                onValueChange = { viewModel.update { copy(cosmeticCondition = it) } },
                label = "Cosmetic Condition",
                suggestions = COSMETIC_CONDITIONS,
                modifier = Modifier.fillMaxWidth()
            )

            AutocompleteTextField(
                value = form.mechanicalCondition,
                onValueChange = { viewModel.update { copy(mechanicalCondition = it) } },
                label = "Mechanical Condition",
                suggestions = MECHANICAL_CONDITIONS,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.repairsNeeded,
                onValueChange = { viewModel.update { copy(repairsNeeded = it) } },
                label = { Text("Repairs Needed") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            SectionLabel("Valuation")

            OutlinedTextField(
                value = form.purchaseDate,
                onValueChange = { viewModel.update { copy(purchaseDate = it) } },
                label = { Text("Purchase Date") },
                singleLine = true,
                placeholder = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = form.purchaseCost,
                    onValueChange = { viewModel.update { copy(purchaseCost = it) } },
                    label = { Text("Purchase Cost") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    prefix = { Text("$") }
                )
                OutlinedTextField(
                    value = form.currentValue,
                    onValueChange = { viewModel.update { copy(currentValue = it) } },
                    label = { Text("Current Value") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    prefix = { Text("$") }
                )
            }

            OutlinedTextField(
                value = form.insuranceValue,
                onValueChange = { viewModel.update { copy(insuranceValue = it) } },
                label = { Text("Insurance Value") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )

            SectionLabel("Features")
            FeatureChips(
                selectedFeatures = form.selectedFeatures,
                onFeatureToggle = viewModel::toggleFeature,
                modifier = Modifier.fillMaxWidth()
            )

            SectionLabel("Additional Details")

            OutlinedTextField(
                value = form.provenance,
                onValueChange = { viewModel.update { copy(provenance = it) } },
                label = { Text("Provenance / History") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = form.location,
                onValueChange = { viewModel.update { copy(location = it) } },
                label = { Text("Storage Location") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.tags,
                onValueChange = { viewModel.update { copy(tags = it) } },
                label = { Text("Tags (comma separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = form.notes,
                onValueChange = { viewModel.update { copy(notes = it) } },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            form.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveItem(onSaved) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !form.isSaving
            ) {
                if (form.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Item")
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
    HorizontalDivider()
}
