package com.watchclock.tracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.watchclock.tracker.WatchClockApplication
import com.watchclock.tracker.data.model.CollectionType
import com.watchclock.tracker.data.repository.BrandRepository
import com.watchclock.tracker.data.repository.ItemRepository
import com.watchclock.tracker.ui.screens.addedit.AddEditScreen
import com.watchclock.tracker.ui.screens.brands.BrandScreen
import com.watchclock.tracker.ui.screens.detail.DetailScreen
import com.watchclock.tracker.ui.screens.home.HomeScreen
import com.watchclock.tracker.ui.screens.list.ListScreen
import com.watchclock.tracker.ui.screens.settings.SettingsScreen
import com.watchclock.tracker.ui.viewmodel.*

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Icons.Filled.Home),
    BottomNavItem(Screen.Collections, "Collections", Icons.Filled.CollectionsBookmark),
    BottomNavItem(Screen.Brands, "Brands", Icons.Filled.MenuBook),
    BottomNavItem(Screen.Settings, "Settings", Icons.Filled.Settings)
)

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as WatchClockApplication
    val db = app.database

    val itemRepository = remember {
        ItemRepository(db.itemDao(), db.photoDao(), db.serviceHistoryDao())
    }
    val brandRepository = remember {
        BrandRepository(db.brandDao(), db.watchModelDao())
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any {
        it.screen.route == currentRoute ||
        (it.screen is Screen.Collections && currentRoute?.startsWith("collections") == true)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = when (item.screen) {
                            is Screen.Collections -> currentRoute?.startsWith("collections") == true
                            else -> currentRoute == item.screen.route
                        }
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(
                                    if (item.screen is Screen.Collections)
                                        Screen.Collections.createRoute()
                                    else item.screen.route
                                ) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                val factory = HomeViewModelFactory(itemRepository)
                val viewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onNavigateToCollections = { type ->
                        navController.navigate(Screen.Collections.createRoute(type))
                    },
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    },
                    onNavigateToAdd = { type ->
                        navController.navigate(Screen.AddEdit.createRoute(type = type))
                    }
                )
            }

            composable(
                route = Screen.Collections.route,
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.StringType
                        defaultValue = "WATCH"
                    }
                )
            ) { backStackEntry ->
                val typeStr = backStackEntry.arguments?.getString("type") ?: "WATCH"
                val initialType = runCatching { CollectionType.valueOf(typeStr) }
                    .getOrDefault(CollectionType.WATCH)
                val factory = ListViewModelFactory(itemRepository, initialType)
                val viewModel: ListViewModel = viewModel(factory = factory)
                val currentType by viewModel.collectionType.collectAsStateWithLifecycle()
                ListScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    },
                    onNavigateToAdd = {
                        navController.navigate(
                            Screen.AddEdit.createRoute(type = currentType.name)
                        )
                    }
                )
            }

            composable(Screen.Brands.route) {
                val factory = BrandViewModelFactory(brandRepository)
                val viewModel: BrandViewModel = viewModel(factory = factory)
                BrandScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            }

            composable(Screen.Settings.route) {
                val factory = SettingsViewModelFactory(itemRepository, context)
                val viewModel: SettingsViewModel = viewModel(factory = factory)
                SettingsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
                val factory = DetailViewModelFactory(itemRepository, itemId)
                val viewModel: DetailViewModel = viewModel(factory = factory)
                DetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id, type ->
                        navController.navigate(Screen.AddEdit.createRoute(itemId = id, type = type))
                    }
                )
            }

            composable(
                route = Screen.AddEdit.route,
                arguments = listOf(
                    navArgument("itemId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    },
                    navArgument("type") {
                        type = NavType.StringType
                        defaultValue = "WATCH"
                    }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getLong("itemId")
                    ?.takeIf { it != -1L }
                val typeStr = backStackEntry.arguments?.getString("type") ?: "WATCH"
                val factory = AddEditViewModelFactory(itemRepository, brandRepository, itemId, typeStr)
                val viewModel: AddEditViewModel = viewModel(factory = factory)
                AddEditScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
        }
    }
}
