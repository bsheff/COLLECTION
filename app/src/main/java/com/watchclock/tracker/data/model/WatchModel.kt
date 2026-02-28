package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "watch_models",
    foreignKeys = [
        ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("brandId")]
)
data class WatchModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandId: Long,
    val name: String,
    val reference: String? = null,
    val type: CollectionType
)
