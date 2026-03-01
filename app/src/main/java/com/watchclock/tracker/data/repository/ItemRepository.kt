package com.watchclock.tracker.data.repository

import com.watchclock.tracker.data.database.dao.ItemDao
import com.watchclock.tracker.data.database.dao.PhotoDao
import com.watchclock.tracker.data.database.dao.ServiceHistoryDao
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.model.Photo
import com.watchclock.tracker.data.model.ServiceHistory
import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val itemDao: ItemDao,
    private val photoDao: PhotoDao,
    private val serviceHistoryDao: ServiceHistoryDao
) {

    fun getAllItems(): Flow<List<CollectionItem>> = itemDao.getAllItems()

    fun getItemsByType(type: CollectionType): Flow<List<CollectionItem>> =
        itemDao.getItemsByType(type)

    fun getItemById(id: Long): Flow<CollectionItem?> = itemDao.getItemById(id)

    fun searchItems(type: CollectionType?, brand: String?, query: String?): Flow<List<CollectionItem>> =
        itemDao.searchItems(type, brand, query)

    fun getCountByType(type: CollectionType): Flow<Int> = itemDao.getCountByType(type)

    fun getTotalValueByType(type: CollectionType): Flow<Double?> = itemDao.getTotalValueByType(type)

    fun getTotalCostByType(type: CollectionType): Flow<Double?> = itemDao.getTotalCostByType(type)

    fun getBrandsByType(type: CollectionType): Flow<List<String>> = itemDao.getBrandsByType(type)

    suspend fun insertItem(item: CollectionItem): Long = itemDao.insertItem(item)

    suspend fun updateItem(item: CollectionItem) {
        itemDao.updateItem(item.copy(lastModified = System.currentTimeMillis()))
    }

    suspend fun deleteItem(item: CollectionItem) = itemDao.deleteItem(item)

    suspend fun deleteItemById(id: Long) = itemDao.deleteItemById(id)

    suspend fun getAllItemsSync(): List<CollectionItem> = itemDao.getAllItemsSync()

    // Photo operations
    fun getPhotosForItem(itemId: Long): Flow<List<Photo>> = photoDao.getPhotosForItem(itemId)

    suspend fun insertPhoto(photo: Photo): Long = photoDao.insertPhoto(photo)

    suspend fun deletePhoto(photo: Photo) = photoDao.deletePhoto(photo)

    suspend fun setPrimaryPhoto(photo: Photo) = photoDao.setPrimaryPhoto(photo)

    // Service history operations
    fun getServiceHistoryForItem(itemId: Long): Flow<List<ServiceHistory>> =
        serviceHistoryDao.getServiceHistoryForItem(itemId)

    suspend fun insertService(service: ServiceHistory): Long =
        serviceHistoryDao.insertService(service)

    suspend fun updateService(service: ServiceHistory) =
        serviceHistoryDao.updateService(service)

    suspend fun deleteService(service: ServiceHistory) =
        serviceHistoryDao.deleteService(service)
}
