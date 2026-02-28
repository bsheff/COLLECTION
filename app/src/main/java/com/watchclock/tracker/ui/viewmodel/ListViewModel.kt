package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.ItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val repository: ItemRepository,
    val collectionType: CollectionType
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedBrand = MutableStateFlow<String?>(null)
    val selectedBrand: StateFlow<String?> = _selectedBrand.asStateFlow()

    private val _isGridView = MutableStateFlow(false)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<CollectionItem>> = combine(
        _searchQuery, _selectedBrand
    ) { query, brand -> Pair(query, brand) }
        .flatMapLatest { (query, brand) ->
            repository.searchItems(
                type = collectionType,
                brand = brand,
                query = query.ifBlank { null }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val brands: StateFlow<List<String>> = repository.getBrandsByType(collectionType)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun setSelectedBrand(brand: String?) { _selectedBrand.value = brand }

    fun toggleViewMode() { _isGridView.value = !_isGridView.value }
}

class ListViewModelFactory(
    private val repository: ItemRepository,
    private val type: CollectionType
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ListViewModel(repository, type) as T
    }
}
