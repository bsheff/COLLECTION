package com.watchclock.tracker.data.database.dao

import androidx.room.*
import com.watchclock.tracker.data.model.ServiceHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceHistoryDao {

    @Query("SELECT * FROM service_history WHERE itemId = :itemId ORDER BY serviceDate DESC")
    fun getServiceHistoryForItem(itemId: Long): Flow<List<ServiceHistory>>

    @Query("SELECT * FROM service_history WHERE id = :id")
    suspend fun getServiceById(id: Long): ServiceHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceHistory): Long

    @Update
    suspend fun updateService(service: ServiceHistory)

    @Delete
    suspend fun deleteService(service: ServiceHistory)

    @Query("DELETE FROM service_history WHERE itemId = :itemId")
    suspend fun deleteServiceHistoryForItem(itemId: Long)
}
