package com.roxana.recipeapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roxana.recipeapp.add.AddRecipeScreen
import com.roxana.recipeapp.home.HomeScreen

@Composable
fun RecipeNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavAddRecipe = { navController.navigate(Screen.AddRecipe.route) }
            )
        }
        composable(route = Screen.AddRecipe.route) {
            AddRecipeScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddRecipe : Screen("add")
}
