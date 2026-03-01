package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_history",
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
data class ServiceHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val serviceDate: String,
    val servicedBy: String? = null,
    val serviceType: String,
    val cost: Double? = null,
    val notes: String? = null,
    val nextServiceDue: String? = null
)
