package com.watchclock.tracker.data.database.dao

import androidx.room.*
import com.watchclock.tracker.data.model.CollectionItem
import com.watchclock.tracker.data.model.CollectionType
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM collection_items ORDER BY lastModified DESC")
    fun getAllItems(): Flow<List<CollectionItem>>

    @Query("SELECT * FROM collection_items WHERE type = :type ORDER BY lastModified DESC")
    fun getItemsByType(type: CollectionType): Flow<List<CollectionItem>>

    @Query("SELECT * FROM collection_items WHERE id = :id")
    fun getItemById(id: Long): Flow<CollectionItem?>

    @Query("""
        SELECT * FROM collection_items
        WHERE (:type IS NULL OR type = :type)
        AND (:brand IS NULL OR brand LIKE '%' || :brand || '%')
        AND (:query IS NULL OR brand LIKE '%' || :query || '%'
             OR model LIKE '%' || :query || '%'
             OR serialNumber LIKE '%' || :query || '%'
             OR notes LIKE '%' || :query || '%'
             OR tags LIKE '%' || :query || '%')
        ORDER BY lastModified DESC
    """)
    fun searchItems(type: CollectionType?, brand: String?, query: String?): Flow<List<CollectionItem>>

    @Query("SELECT COUNT(*) FROM collection_items WHERE type = :type")
    fun getCountByType(type: CollectionType): Flow<Int>

    @Query("SELECT SUM(currentValue) FROM collection_items WHERE type = :type")
    fun getTotalValueByType(type: CollectionType): Flow<Double?>

    @Query("SELECT SUM(purchaseCost) FROM collection_items WHERE type = :type")
    fun getTotalCostByType(type: CollectionType): Flow<Double?>

    @Query("SELECT DISTINCT brand FROM collection_items WHERE type = :type ORDER BY brand")
    fun getBrandsByType(type: CollectionType): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CollectionItem): Long

    @Update
    suspend fun updateItem(item: CollectionItem)

    @Delete
    suspend fun deleteItem(item: CollectionItem)

    @Query("DELETE FROM collection_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("SELECT * FROM collection_items ORDER BY lastModified DESC")
    suspend fun getAllItemsSync(): List<CollectionItem>
}
