package com.watchclock.tracker.data.database.dao

import androidx.room.*
import com.watchclock.tracker.data.model.Brand
import com.watchclock.tracker.data.model.CollectionType
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {

    @Query("SELECT * FROM brands ORDER BY name")
    fun getAllBrands(): Flow<List<Brand>>

    @Query("SELECT * FROM brands WHERE type = :type ORDER BY name")
    fun getBrandsByType(type: CollectionType): Flow<List<Brand>>

    @Query("SELECT * FROM brands WHERE id = :id")
    suspend fun getBrandById(id: Long): Brand?

    @Query("SELECT * FROM brands WHERE name LIKE '%' || :query || '%' AND type = :type ORDER BY name")
    fun searchBrands(query: String, type: CollectionType): Flow<List<Brand>>

    @Query("SELECT * FROM brands WHERE type = :type ORDER BY name")
    suspend fun getBrandsByTypeSync(type: CollectionType): List<Brand>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBrand(brand: Brand): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBrands(brands: List<Brand>)

    @Update
    suspend fun updateBrand(brand: Brand)

    @Delete
    suspend fun deleteBrand(brand: Brand)

    @Query("SELECT COUNT(*) FROM brands WHERE isCustom = 0")
    suspend fun getDefaultBrandCount(): Int
}
