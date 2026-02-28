package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.*

data class HomeStats(
    val watchCount: Int = 0,
    val clockCount: Int = 0,
    val totalWatchValue: Double? = null,
    val totalClockValue: Double? = null,
    val totalWatchCost: Double? = null,
    val totalClockCost: Double? = null
)

class HomeViewModel(private val repository: ItemRepository) : ViewModel() {

    val recentItems = repository.getAllItems()
        .map { it.take(10) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<HomeStats> = combine(
        repository.getCountByType(CollectionType.WATCH),
        repository.getCountByType(CollectionType.CLOCK),
        repository.getTotalValueByType(CollectionType.WATCH),
        repository.getTotalValueByType(CollectionType.CLOCK),
        repository.getTotalCostByType(CollectionType.WATCH),
        repository.getTotalCostByType(CollectionType.CLOCK)
    ) { values ->
        HomeStats(
            watchCount = values[0] as Int,
            clockCount = values[1] as Int,
            totalWatchValue = values[2] as? Double,
            totalClockValue = values[3] as? Double,
            totalWatchCost = values[4] as? Double,
            totalClockCost = values[5] as? Double
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeStats())
}

class HomeViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(repository) as T
    }
}
