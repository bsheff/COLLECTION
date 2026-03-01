package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_items")
data class CollectionItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: CollectionType,
    val brand: String,
    val model: String,
    val serialNumber: String = "",
    val yearOfManufacture: Int? = null,
    val era: String? = null,
    val category: String? = null,
    val purchaseDate: String? = null,
    val purchaseCost: Double? = null,
    val currentValue: Double? = null,
    val insuranceValue: Double? = null,
    val cosmeticCondition: String? = null,
    val mechanicalCondition: String? = null,
    val movementType: String? = null,
    val features: String = "[]",
    val repairsNeeded: String? = null,
    val notes: String? = null,
    val provenance: String? = null,
    val location: String? = null,
    val tags: String? = null,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)
