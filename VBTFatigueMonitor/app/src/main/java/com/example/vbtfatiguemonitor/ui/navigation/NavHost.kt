package com.example.vbtfatiguemonitor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vbtfatiguemonitor.ui.screens.DetailScreen
import com.example.vbtfatiguemonitor.ui.screens.HomeScreen
import com.example.vbtfatiguemonitor.ui.screens.TrainingScreen

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Training : Screen("training_screen")
    object Detail : Screen("detail_screen/{serieId}") {
        fun createRoute(serieId: Long) = "detail_screen/$serieId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTraining = { navController.navigate(Screen.Training.route) },
                onNavigateToDetail = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
            )
        }
        composable(Screen.Training.route) {
            TrainingScreen(
                onFinishTraining = {
                    // Tras guardar, vuelve al historial (Home).
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("serieId") { type = NavType.LongType })
        ) { backStackEntry ->
            val serieId = backStackEntry.arguments?.getLong("serieId") ?: 0L
            DetailScreen(
                serieId = serieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
