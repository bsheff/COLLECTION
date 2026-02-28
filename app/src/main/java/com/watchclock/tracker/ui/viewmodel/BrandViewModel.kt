package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.Brand
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.BrandRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrandViewModel(private val repository: BrandRepository) : ViewModel() {

    private val _selectedType = MutableStateFlow(CollectionType.WATCH)
    val selectedType: StateFlow<CollectionType> = _selectedType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val brands: StateFlow<List<Brand>> = combine(_selectedType, _searchQuery) { type, query ->
        type to query
    }.flatMapLatest { (type, query) ->
        if (query.isBlank()) repository.getBrandsByType(type)
        else repository.searchBrands(query, type)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setType(type: CollectionType) { _selectedType.value = type }

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun addBrand(brand: Brand) {
        viewModelScope.launch { repository.insertBrand(brand) }
    }

    fun deleteBrand(brand: Brand) {
        viewModelScope.launch { repository.deleteBrand(brand) }
    }

    fun updateBrand(brand: Brand) {
        viewModelScope.launch { repository.updateBrand(brand) }
    }
}

class BrandViewModelFactory(private val repository: BrandRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrandViewModel(repository) as T
    }
}
