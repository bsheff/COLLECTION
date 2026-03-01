package com.watchclock.tracker.data.database.dao

import androidx.room.*
import com.watchclock.tracker.data.model.WatchModel
import com.watchclock.tracker.data.model.CollectionType
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchModelDao {

    @Query("SELECT * FROM watch_models WHERE brandId = :brandId ORDER BY name")
    fun getModelsByBrand(brandId: Long): Flow<List<WatchModel>>

    @Query("SELECT * FROM watch_models WHERE type = :type ORDER BY name")
    fun getModelsByType(type: CollectionType): Flow<List<WatchModel>>

    @Query("SELECT * FROM watch_models WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchModels(query: String): Flow<List<WatchModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: WatchModel): Long

    @Update
    suspend fun updateModel(model: WatchModel)

    @Delete
    suspend fun deleteModel(model: WatchModel)
}
