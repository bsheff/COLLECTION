package com.watchclock.tracker.ui.viewmodel

import androidx.lifecycle.*
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.Photo
import com.watchclock.tracker.data.model.ServiceHistory
import com.watchclock.tracker.data.repository.ItemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: ItemRepository,
    private val itemId: Long
) : ViewModel() {

    val item: StateFlow<CollectionItem?> = repository.getItemById(itemId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val photos: StateFlow<List<Photo>> = repository.getPhotosForItem(itemId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceHistory: StateFlow<List<ServiceHistory>> = repository.getServiceHistoryForItem(itemId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteItem(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.deleteItemById(itemId)
            onComplete()
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
        }
    }

    fun addServiceRecord(service: ServiceHistory) {
        viewModelScope.launch {
            repository.insertService(service)
        }
    }

    fun deleteServiceRecord(service: ServiceHistory) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }
}

class DetailViewModelFactory(
    private val repository: ItemRepository,
    private val itemId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DetailViewModel(repository, itemId) as T
    }
}
