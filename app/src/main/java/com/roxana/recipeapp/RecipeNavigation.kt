package com.roxana.recipeapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.roxana.recipeapp.add.AddRecipeScreen
import com.roxana.recipeapp.add.AddRecipeViewModel
import com.roxana.recipeapp.comment.AddCommentScreen
import com.roxana.recipeapp.comment.AddCommentViewModel
import com.roxana.recipeapp.cooking.CookingScreen
import com.roxana.recipeapp.cooking.CookingViewModel
import com.roxana.recipeapp.cooking.ingredients.VaryIngredientsScreen
import com.roxana.recipeapp.cooking.ingredients.VaryIngredientsViewModel
import com.roxana.recipeapp.detail.DetailScreen
import com.roxana.recipeapp.detail.DetailViewModel
import com.roxana.recipeapp.home.HomeScreen
import com.roxana.recipeapp.home.HomeViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RecipeNavigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
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
            ) { backStackEntry ->
                val detailViewModel = hiltViewModel<DetailViewModel>()
                DetailScreen(
                    detailViewModel = detailViewModel,
                    onStartCooking = {
                        navController.navigate(
                            Screen.Cooking.destination(
                                Screen.Cooking.Arguments(
                                    backStackEntry.arguments!!.getInt(Screen.RecipeDetail.KEY_ID)
                                )
                            )
                        )
                    },
                    onBack = { navController.navigateUp() },
                    onAddComment = {
                        navController.navigate(
                            Screen.AddComment.destination(
                                Screen.AddComment.Arguments(
                                    backStackEntry.arguments!!.getInt(Screen.RecipeDetail.KEY_ID)
                                )
                            )
                        )
                    }
                )
            }
            composable(
                route = Screen.Cooking.route,
                arguments = Screen.Cooking.arguments
            ) { backStackEntry ->
                val cookingViewModel = hiltViewModel<CookingViewModel>()
                CookingScreen(
                    cookingViewModel = cookingViewModel,
                    onBack = { navController.navigateUp() },
                    onAddComment = {
                        navController.navigate(
                            Screen.AddComment.destination(
                                Screen.AddComment.Arguments(
                                    backStackEntry.arguments!!.getInt(Screen.Cooking.KEY_ID)
                                )
                            )
                        )
                    },
                    onVaryIngredient = {
                        navController.navigate(
                            Screen.VaryIngredientQuantities.destination(
                                Screen.VaryIngredientQuantities.Arguments(
                                    backStackEntry.arguments!!.getInt(Screen.Cooking.KEY_ID)
                                )
                            )
                        )
                    }
                )
            }
            bottomSheet(
                route = Screen.AddComment.route,
                arguments = Screen.AddComment.arguments
            ) {
                val addCommentViewModel = hiltViewModel<AddCommentViewModel>()
                AddCommentScreen(
                    addCommentViewModel = addCommentViewModel,
                    onBack = { navController.navigateUp() }
                )
            }
            bottomSheet(
                route = Screen.VaryIngredientQuantities.route,
                arguments = Screen.VaryIngredientQuantities.arguments
            ) {
                val varyIngredientsViewModel = hiltViewModel<VaryIngredientsViewModel>()
                VaryIngredientsScreen(
                    varyIngredientsViewModel = varyIngredientsViewModel
                ) { quantityMultiplier, recipeId ->
                    navController.popBackStack(Screen.Cooking.route, true)
                    navController.navigate(
                        Screen.Cooking.destination(
                            Screen.Cooking.Arguments(recipeId, quantityMultiplier.toString())
                        )
                    )
                }
            }
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

    object Cooking : Screen("cooking"), NavRoute, NavDestination<Cooking.Arguments> {
        const val KEY_ID = "id"
        const val KEY_PORTIONS_MULTIPLIER = "portions_multiplier"

        override val route: String =
            "$rootRoute/{$KEY_ID}?$KEY_PORTIONS_MULTIPLIER={$KEY_PORTIONS_MULTIPLIER}"
        override val arguments: List<NamedNavArgument> =
            listOf(
                navArgument(KEY_ID) { type = NavType.IntType },
                navArgument(KEY_PORTIONS_MULTIPLIER) { type = NavType.StringType }
            )

        override fun destination(arguments: Arguments): String =
            "$rootRoute/${arguments.id}?$KEY_PORTIONS_MULTIPLIER=${arguments.portionsMultiplier}"

        data class Arguments(val id: Int, val portionsMultiplier: String = "1")
    }

    object AddComment :
        Screen("add_comment"),
        NavRoute,
        NavDestination<AddComment.Arguments> {
        const val KEY_ID = "id"

        override val route: String = "$rootRoute/{$KEY_ID}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY_ID) { type = NavType.IntType })

        override fun destination(arguments: Arguments): String = "$rootRoute/${arguments.id}"

        data class Arguments(val id: Int)
    }

    object VaryIngredientQuantities :
        Screen("ingredient_quantities"),
        NavRoute,
        NavDestination<VaryIngredientQuantities.Arguments> {
        const val KEY_RECIPE_ID = "recipe_id"

        override val route: String = "$rootRoute/{$KEY_RECIPE_ID}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument(KEY_RECIPE_ID) { type = NavType.IntType })

        override fun destination(arguments: Arguments): String = "$rootRoute/${arguments.id}"

        data class Arguments(val id: Int)
    }
}
