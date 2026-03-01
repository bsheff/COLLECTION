package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.ItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val repository: ItemRepository,
    initialType: CollectionType
) : ViewModel() {

    private val _collectionType = MutableStateFlow(initialType)
    val collectionType: StateFlow<CollectionType> = _collectionType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedBrand = MutableStateFlow<String?>(null)
    val selectedBrand: StateFlow<String?> = _selectedBrand.asStateFlow()

    private val _isGridView = MutableStateFlow(false)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<CollectionItem>> = combine(
        _collectionType, _searchQuery, _selectedBrand
    ) { type, query, brand -> Triple(type, query, brand) }
        .flatMapLatest { (type, query, brand) ->
            repository.searchItems(
                type = type,
                brand = brand,
                query = query.ifBlank { null }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val brands: StateFlow<List<String>> = _collectionType
        .flatMapLatest { type -> repository.getBrandsByType(type) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCollectionType(type: CollectionType) {
        if (_collectionType.value != type) {
            _collectionType.value = type
            _selectedBrand.value = null
            _searchQuery.value = ""
        }
    }

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
