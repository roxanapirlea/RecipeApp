package com.roxana.recipeapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roxana.recipeapp.add.AddRecipeScreen
import com.roxana.recipeapp.add.AddRecipeViewModel
import com.roxana.recipeapp.home.HomeScreen
import com.roxana.recipeapp.home.HomeViewModel

@Composable
fun RecipeNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                homeViewModel = homeViewModel,
                onNavAddRecipe = { navController.navigate(Screen.AddRecipe.route) }
            )
        }
        composable(route = Screen.AddRecipe.route) {
            val addRecipeViewModel = hiltViewModel<AddRecipeViewModel>()
            AddRecipeScreen(
                addRecipeViewModel = addRecipeViewModel,
                onBack = { navController.navigateUp() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddRecipe : Screen("add")
}
