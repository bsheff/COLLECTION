package com.watchclock.tracker.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.watchclock.tracker.data.repository.ItemRepository
import com.watchclock.tracker.util.ExportHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsState(
    val isExporting: Boolean = false,
    val lastExportPath: String? = null,
    val exportError: String? = null
)

class SettingsViewModel(
    private val repository: ItemRepository,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun exportCsv() {
        _state.value = _state.value.copy(isExporting = true, exportError = null)
        viewModelScope.launch {
            val items = repository.getAllItemsSync()
            val path = ExportHelper.exportCsv(context, items)
            _state.value = _state.value.copy(
                isExporting = false,
                lastExportPath = path,
                exportError = if (path == null) "CSV export failed" else null
            )
        }
    }

    fun exportJson() {
        _state.value = _state.value.copy(isExporting = true, exportError = null)
        viewModelScope.launch {
            val items = repository.getAllItemsSync()
            val path = ExportHelper.exportJson(context, items)
            _state.value = _state.value.copy(
                isExporting = false,
                lastExportPath = path,
                exportError = if (path == null) "JSON export failed" else null
            )
        }
    }

    fun clearExportStatus() {
        _state.value = _state.value.copy(lastExportPath = null, exportError = null)
    }
}

class SettingsViewModelFactory(
    private val repository: ItemRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(repository, context) as T
    }
}
