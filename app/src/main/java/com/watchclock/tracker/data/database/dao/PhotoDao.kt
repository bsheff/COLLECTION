package com.watchclock.tracker.data.database.dao

import androidx.room.*
import com.watchclock.tracker.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE itemId = :itemId ORDER BY isPrimary DESC, dateAdded ASC")
    fun getPhotosForItem(itemId: Long): Flow<List<Photo>>

    @Query("SELECT * FROM photos WHERE itemId = :itemId AND isPrimary = 1 LIMIT 1")
    suspend fun getPrimaryPhoto(itemId: Long): Photo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo): Long

    @Update
    suspend fun updatePhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query("DELETE FROM photos WHERE itemId = :itemId")
    suspend fun deletePhotosForItem(itemId: Long)

    @Query("UPDATE photos SET isPrimary = 0 WHERE itemId = :itemId")
    suspend fun clearPrimaryForItem(itemId: Long)

    @Transaction
    suspend fun setPrimaryPhoto(photo: Photo) {
        clearPrimaryForItem(photo.itemId)
        updatePhoto(photo.copy(isPrimary = true))
    }
}
