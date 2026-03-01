package com.watchclock.tracker.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watchclock.tracker.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Show snackbar for export results
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.lastExportPath, state.exportError) {
        when {
            state.lastExportPath != null -> {
                snackbarHostState.showSnackbar("Exported to: ${state.lastExportPath}")
                viewModel.clearExportStatus()
            }
            state.exportError != null -> {
                snackbarHostState.showSnackbar("Error: ${state.exportError}")
                viewModel.clearExportStatus()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                "Export Collection",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider()

            Text(
                "Export your collection data to the Downloads folder.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = { viewModel.exportCsv() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isExporting
            ) {
                if (state.isExporting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Icon(Icons.Filled.TableChart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Export as CSV")
            }

            OutlinedButton(
                onClick = { viewModel.exportJson() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isExporting
            ) {
                if (state.isExporting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Icon(Icons.Filled.Code, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Export as JSON")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "About",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider()

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Watch, contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Watch & Clock Tracker", style = MaterialTheme.typography.titleSmall)
                    }
                    Text("Version 1.0", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        "Track and manage your antique watch and clock collection.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
