package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.*

data class HomeStats(
    val countByType: Map<CollectionType, Int> = emptyMap(),
    val totalValue: Double? = null,
    val totalCost: Double? = null
)

class HomeViewModel(private val repository: ItemRepository) : ViewModel() {

    val recentItems = repository.getAllItems()
        .map { it.take(10) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<HomeStats> = repository.getAllItems()
        .map { items ->
            val byType = CollectionType.entries.associateWith { type ->
                items.count { it.type == type }
            }.filterValues { it > 0 }
            HomeStats(
                countByType = byType,
                totalValue = items.mapNotNull { it.currentValue }
                    .takeIf { it.isNotEmpty() }?.sum(),
                totalCost = items.mapNotNull { it.purchaseCost }
                    .takeIf { it.isNotEmpty() }?.sum()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeStats())
}

class HomeViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(repository) as T
    }
}
