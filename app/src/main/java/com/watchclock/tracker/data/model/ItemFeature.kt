package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "item_features",
    primaryKeys = ["itemId", "feature"],
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
data class ItemFeature(
    val itemId: Long,
    val feature: String
)
