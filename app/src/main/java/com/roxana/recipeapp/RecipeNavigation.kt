package com.roxana.recipeapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.roxana.recipeapp.add.AddRecipeScreen
import com.roxana.recipeapp.add.AddRecipeViewModel
import com.roxana.recipeapp.detail.DetailScreen
import com.roxana.recipeapp.detail.DetailViewModel
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
                onNavDetail = {
                    navController.navigate(
                        Screen.RecipeDetail.destination(Screen.RecipeDetail.Arguments(it))
                    )
                },
                onNavAddRecipe = { navController.navigate(Screen.AddRecipe.destination(null)) }
            )
        }
        composable(route = Screen.AddRecipe.route) {
            val addRecipeViewModel = hiltViewModel<AddRecipeViewModel>()
            AddRecipeScreen(
                addRecipeViewModel = addRecipeViewModel,
                onBack = { navController.navigateUp() }
            )
        }
        composable(
            route = Screen.RecipeDetail.route,
            arguments = Screen.RecipeDetail.arguments
        ) {
            val detailViewModel = hiltViewModel<DetailViewModel>()
            DetailScreen(
                detailViewModel = detailViewModel,
                onBack = { navController.navigateUp() }
            )
        }
    }
}

interface NavRoute {
    val route: String
    val arguments: List<NamedNavArgument>
}

interface NavDestination<T> {
    fun destination(arguments: T): String
}

sealed class Screen(val rootRoute: String) {
    object Home : Screen("home"), NavRoute, NavDestination<Any?> {
        override val route: String = rootRoute
        override val arguments: List<NamedNavArgument> = emptyList()
        override fun destination(arguments: Any?): String = rootRoute
    }

    object AddRecipe : Screen("add"), NavRoute, NavDestination<Any?> {
        override val route: String = rootRoute
        override val arguments: List<NamedNavArgument> = emptyList()
        override fun destination(arguments: Any?): String = rootRoute
    }

    object RecipeDetail : Screen("detail"), NavRoute, NavDestination<RecipeDetail.Arguments> {
        const val KEY_ID = "id"

        override val route: String = "$rootRoute/{$KEY_ID}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY_ID) { type = NavType.IntType })

        override fun destination(arguments: Arguments): String = "$rootRoute/${arguments.id}"

        data class Arguments(val id: Int)
    }
}
