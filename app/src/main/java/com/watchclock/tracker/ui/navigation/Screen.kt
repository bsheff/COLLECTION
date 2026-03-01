package com.watchclock.tracker.ui.navigation

import com.watchclock.tracker.data.model.CollectionType

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Collections : Screen("collections?type={type}") {
        fun createRoute(type: CollectionType = CollectionType.WATCH) =
            "collections?type=${type.name}"
    }
    object Brands : Screen("brands")
    object Settings : Screen("settings")

    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Long) = "detail/$itemId"
    }

    object AddEdit : Screen("addedit?itemId={itemId}&type={type}") {
        fun createRoute(itemId: Long? = null, type: String = "WATCH") =
            if (itemId != null) "addedit?itemId=$itemId&type=$type"
            else "addedit?type=$type"
    }
}
