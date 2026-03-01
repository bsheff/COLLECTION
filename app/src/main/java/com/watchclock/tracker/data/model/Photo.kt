package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = CollectionItem::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("itemId")]
)
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val filePath: String,
    val caption: String? = null,
    val isPrimary: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)
