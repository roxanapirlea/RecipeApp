package com.roxana.recipeapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.roxana.recipeapp.comment.AddCommentDestination
import com.roxana.recipeapp.comment.AddCommentViewModel
import com.roxana.recipeapp.cooking.CookingDestination
import com.roxana.recipeapp.cooking.CookingViewModel
import com.roxana.recipeapp.cooking.ingredients.VaryIngredientsDestination
import com.roxana.recipeapp.cooking.ingredients.VaryIngredientsViewModel
import com.roxana.recipeapp.detail.DetailDestination
import com.roxana.recipeapp.detail.DetailViewModel
import com.roxana.recipeapp.home.HomeDestination
import com.roxana.recipeapp.home.HomeViewModel
import com.roxana.recipeapp.settings.SettingsDestination
import com.roxana.recipeapp.settings.SettingsViewModel
import com.roxana.recipeapp.settings.debug.DebugSettingsDestination
import com.roxana.recipeapp.settings.debug.DebugSettingsViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RecipeNavigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, startDestination = HomeNode.route) {
            editRecipeGraph(navController)

            composable(route = HomeNode.route) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeDestination(
                    homeViewModel = homeViewModel,
                    onNavDetail = { navController.navigate(RecipeDetailNode.destination(it)) },
                    onNavAddRecipe = { navController.navigate(EditGraphRootNode.route) },
                    onNavSettings = { navController.navigate(SettingsNode.route) },
                )
            }
            composable(route = SettingsNode.route) {
                val settingsViewModel = hiltViewModel<SettingsViewModel>()
                SettingsDestination(
                    settingsViewModel = settingsViewModel,
                    onNavBack = { navController.navigateUp() },
                    onNavDebugSettings = { navController.navigate(DebugSettingsNode.route) }
                )
            }
            composable(route = DebugSettingsNode.route) {
                val debugSettingsViewModel = hiltViewModel<DebugSettingsViewModel>()
                DebugSettingsDestination(
                    debugSettingsViewModel = debugSettingsViewModel,
                    onBack = { navController.navigateUp() }
                )
            }
            composable(
                route = RecipeDetailNode.route,
                arguments = RecipeDetailNode.args
            ) { backStackEntry ->
                val detailViewModel = hiltViewModel<DetailViewModel>()
                val recipeId = backStackEntry.arguments!!.getInt(RecipeDetailNode.KEY_ID)
                DetailDestination(
                    detailViewModel = detailViewModel,
                    onNavStartCooking = { navController.navigate(CookingNode.destination(recipeId)) },
                    onNavBack = { navController.navigateUp() },
                    onNavAddComment = { navController.navigate(AddCommentNode.destination(recipeId)) },
                    onNavEdit = { navController.navigate(EditGraphRootNode.route) }
                )
            }
            composable(
                route = CookingNode.route,
                arguments = CookingNode.args
            ) { backStackEntry ->
                val cookingViewModel = hiltViewModel<CookingViewModel>()
                val recipeId = backStackEntry.arguments!!.getInt(CookingNode.KEY_ID)
                CookingDestination(
                    cookingViewModel = cookingViewModel,
                    onNavBack = { navController.navigateUp() },
                    onNavAddComment = { navController.navigate(AddCommentNode.destination(recipeId)) },
                    onNavVaryIngredient = {
                        navController.navigate(VaryIngredientQuantitiesNode.destination(recipeId))
                    }
                )
            }
            bottomSheet(
                route = AddCommentNode.route,
                arguments = AddCommentNode.args
            ) {
                val addCommentViewModel = hiltViewModel<AddCommentViewModel>()
                AddCommentDestination(
                    addCommentViewModel = addCommentViewModel,
                    onNavBack = { navController.navigateUp() }
                )
            }
            bottomSheet(
                route = VaryIngredientQuantitiesNode.route,
                arguments = VaryIngredientQuantitiesNode.args
            ) {
                val varyIngredientsViewModel = hiltViewModel<VaryIngredientsViewModel>()
                VaryIngredientsDestination(
                    varyIngredientsViewModel = varyIngredientsViewModel
                ) { quantityMultiplier, recipeId ->
                    navController.navigate(
                        CookingNode.destination(recipeId, quantityMultiplier.toString())
                    ) {
                        popUpTo(CookingNode.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
