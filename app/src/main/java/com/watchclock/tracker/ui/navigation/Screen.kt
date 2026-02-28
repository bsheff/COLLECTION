package com.watchclock.tracker.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Watches : Screen("watches")
    object Clocks : Screen("clocks")
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
