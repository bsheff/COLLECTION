package com.watchclock.tracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.watchclock.tracker.data.model.CollectionType

fun CollectionType.icon(): ImageVector = when (this) {
    CollectionType.WATCH -> Icons.Filled.Watch
    CollectionType.CLOCK -> Icons.Filled.Schedule
    CollectionType.BOOK -> Icons.Filled.MenuBook
    CollectionType.ANTIQUE_FURNITURE -> Icons.Filled.Chair
    CollectionType.COIN -> Icons.Filled.MonetizationOn
    CollectionType.STAMP -> Icons.Filled.LocalPostOffice
    CollectionType.JEWELRY -> Icons.Filled.Diamond
    CollectionType.OTHER -> Icons.Filled.Category
}
