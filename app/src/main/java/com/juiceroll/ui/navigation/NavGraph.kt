package com.juiceroll.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juiceroll.ui.screens.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DICE = "dice"        // Future
    const val PRESETS = "presets"  // Future
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}
