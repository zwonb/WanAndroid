package me.zwonb.wanandroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.zwonb.wanandroid.navigation.WanNavHost
import me.zwonb.wanandroid.navigation.home
import me.zwonb.wanandroid.navigation.me
import me.zwonb.wanandroid.navigation.square

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WanApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar(navController)) {
                WanBottomBar(navController)
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        WanNavHost(navController, Modifier.padding(padding).consumedWindowInsets(padding))
    }
}

@Composable
private fun WanBottomBar(navController: NavHostController) {
    NavigationBar {
        val destinations = remember { MainDestination.values().toList() }
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        destinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val icon = if (selected) destination.selectedIcon else destination.unselectedIcon
                    Icon(icon, null)
                },
                label = { Text(destination.titleText) }
            )
        }
    }
}

enum class MainDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val titleText: String,
    val route: String
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleText = "首页",
        route = home
    ),
    SQUARE(
        selectedIcon = Icons.Filled.Square,
        unselectedIcon = Icons.Outlined.Square,
        titleText = "广场",
        route = square
    ),
    ME(
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        titleText = "我的",
        route = me
    )
}

@Composable
private fun showBottomBar(navController: NavHostController) =
    when (navController.currentBackStackEntryAsState().value?.destination?.route) {
        home, square, me -> true
        else -> false
    }
