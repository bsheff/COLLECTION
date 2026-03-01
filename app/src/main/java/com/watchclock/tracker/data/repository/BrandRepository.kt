package com.watchclock.tracker.data.repository

import com.watchclock.tracker.data.database.dao.BrandDao
import com.watchclock.tracker.data.database.dao.WatchModelDao
import com.watchclock.tracker.data.model.Brand
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.model.WatchModel
import kotlinx.coroutines.flow.Flow

class BrandRepository(
    private val brandDao: BrandDao,
    private val watchModelDao: WatchModelDao
) {

    fun getAllBrands(): Flow<List<Brand>> = brandDao.getAllBrands()

    fun getBrandsByType(type: CollectionType): Flow<List<Brand>> = brandDao.getBrandsByType(type)

    fun searchBrands(query: String, type: CollectionType): Flow<List<Brand>> =
        brandDao.searchBrands(query, type)

    suspend fun getBrandsByTypeSync(type: CollectionType): List<Brand> =
        brandDao.getBrandsByTypeSync(type)

    suspend fun insertBrand(brand: Brand): Long = brandDao.insertBrand(brand)

    suspend fun updateBrand(brand: Brand) = brandDao.updateBrand(brand)

    suspend fun deleteBrand(brand: Brand) = brandDao.deleteBrand(brand)

    // Model operations
    fun getModelsByBrand(brandId: Long): Flow<List<WatchModel>> =
        watchModelDao.getModelsByBrand(brandId)

    fun getModelsByType(type: CollectionType): Flow<List<WatchModel>> =
        watchModelDao.getModelsByType(type)

    fun searchModels(query: String): Flow<List<WatchModel>> =
        watchModelDao.searchModels(query)

    suspend fun insertModel(model: WatchModel): Long = watchModelDao.insertModel(model)

    suspend fun deleteModel(model: WatchModel) = watchModelDao.deleteModel(model)
}
