package com.watchclock.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class Brand(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val commonMisspellings: String = "[]",
    val country: String? = null,
    val foundedYear: Int? = null,
    val type: CollectionType,
    val isCustom: Boolean = false
)
