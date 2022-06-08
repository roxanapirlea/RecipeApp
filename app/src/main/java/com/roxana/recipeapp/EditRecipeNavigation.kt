package com.roxana.recipeapp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesDestination
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesViewModel
import com.roxana.recipeapp.edit.comments.EditRecipeCommentsDestination
import com.roxana.recipeapp.edit.comments.EditRecipeCommentsViewModel
import com.roxana.recipeapp.edit.ingredients.EditRecipeIngredientsDestination
import com.roxana.recipeapp.edit.ingredients.EditRecipeIngredientsViewModel
import com.roxana.recipeapp.edit.instructions.EditRecipeInstructionsDestination
import com.roxana.recipeapp.edit.instructions.EditRecipeInstructionsViewModel
import com.roxana.recipeapp.edit.portions.EditRecipePortionsDestination
import com.roxana.recipeapp.edit.portions.EditRecipePortionsViewModel
import com.roxana.recipeapp.edit.recap.RecapDestination
import com.roxana.recipeapp.edit.recap.RecapViewModel
import com.roxana.recipeapp.edit.temperature.EditRecipeTemperatureDestination
import com.roxana.recipeapp.edit.temperature.EditRecipeTemperatureViewModel
import com.roxana.recipeapp.edit.time.EditRecipeTimeDestination
import com.roxana.recipeapp.edit.time.EditRecipeTimeViewModel
import com.roxana.recipeapp.edit.title.EditRecipeTitleDestination
import com.roxana.recipeapp.edit.title.EditRecipeTitleViewModel

fun NavGraphBuilder.editRecipeGraph(navController: NavController) {
    navigation(
        startDestination = EditTitleNode.route,
        route = EditGraphRootNode.route
    ) {

        composable(route = EditTitleNode.route) {
            val editRecipeTitleViewModel = hiltViewModel<EditRecipeTitleViewModel>()
            EditRecipeTitleDestination(
                editRecipeTitleViewModel = editRecipeTitleViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditCategoriesNode.route) },
                onEditNavForward = { navController.navigate(EditCategoriesNode.route) },
                onNavToPage = { navController.popOrNavigate(EditTitleNode, it.toScreen()) },
            )
        }
        composable(route = EditCategoriesNode.route) {
            val editRecipeCategoriesViewModel = hiltViewModel<EditRecipeCategoriesViewModel>()
            EditRecipeCategoriesDestination(
                editRecipeCategoriesViewModel = editRecipeCategoriesViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditPortionsNode.route) },
                onEditNavForward = { navController.navigate(EditPortionsNode.route) },
                onNavToPage = { navController.popOrNavigate(EditCategoriesNode, it.toScreen()) },
            )
        }
        composable(route = EditPortionsNode.route) {
            val editRecipePortionsViewModel = hiltViewModel<EditRecipePortionsViewModel>()
            EditRecipePortionsDestination(
                editRecipePortionsViewModel = editRecipePortionsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditIngredientsNode.route) },
                onEditNavForward = { navController.navigate(EditIngredientsNode.route) },
                onNavToPage = { navController.popOrNavigate(EditPortionsNode, it.toScreen()) },
            )
        }
        composable(route = EditIngredientsNode.route) {
            val editRecipeIngredientsViewModel = hiltViewModel<EditRecipeIngredientsViewModel>()
            EditRecipeIngredientsDestination(
                ingredientsViewModel = editRecipeIngredientsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditInstructionsNode.route) },
                onEditNavForward = { navController.navigate(EditInstructionsNode.route) },
                onNavToPage = { navController.popOrNavigate(EditIngredientsNode, it.toScreen()) },
            )
        }
        composable(route = EditInstructionsNode.route) {
            val instructionsViewModel = hiltViewModel<EditRecipeInstructionsViewModel>()
            EditRecipeInstructionsDestination(
                instructionsViewModel = instructionsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditTemperatureNode.route) },
                onEditNavForward = { navController.navigate(EditTemperatureNode.route) },
                onNavToPage = { navController.popOrNavigate(EditInstructionsNode, it.toScreen()) },
            )
        }
        composable(route = EditTemperatureNode.route) {
            val editRecipeTemperatureViewModel = hiltViewModel<EditRecipeTemperatureViewModel>()
            EditRecipeTemperatureDestination(
                editRecipeTemperatureViewModel = editRecipeTemperatureViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditTimeNode.route) },
                onEditNavForward = { navController.navigate(EditTimeNode.route) },
                onNavToPage = { navController.popOrNavigate(EditTemperatureNode, it.toScreen()) },
            )
        }
        composable(route = EditTimeNode.route) {
            val editRecipeTimeViewModel = hiltViewModel<EditRecipeTimeViewModel>()
            EditRecipeTimeDestination(
                editRecipeTimeViewModel = editRecipeTimeViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.navigate(EditCommentsNode.route) },
                onNavToPage = { navController.popOrNavigate(EditTimeNode, it.toScreen()) },
            )
        }
        composable(route = EditCommentsNode.route) {
            val commentsViewModel = hiltViewModel<EditRecipeCommentsViewModel>()
            EditRecipeCommentsDestination(
                commentsViewModel = commentsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onNavToPage = { navController.popOrNavigate(EditCommentsNode, it.toScreen()) },
            )
        }
        composable(route = EditRecipeRecapNode.route) {
            val recapViewModel = hiltViewModel<RecapViewModel>()
            RecapDestination(
                recapViewModel = recapViewModel,
                onNavBack = { navController.navigateUp() },
                onNavFinish = { navController.popBackStack(HomeNode.route, false) }
            )
        }
    }
}

private fun NavController.popOrNavigate(from: EditNode, to: EditNode) {
    if (from.position <= to.position)
        navigate(to.route)
    else {
        val wasPopped = popBackStack(to.route, false)
        if (!wasPopped) {
            navigate(to.route) { popUpTo(EditTitleNode.route) { inclusive = true } }
        }
    }
}

private fun PageType.toScreen() =
    when (this) {
        PageType.Title -> EditTitleNode
        PageType.Categories -> EditCategoriesNode
        PageType.Portions -> EditPortionsNode
        PageType.Ingredients -> EditIngredientsNode
        PageType.Instructions -> EditInstructionsNode
        PageType.Temperature -> EditTemperatureNode
        PageType.Time -> EditTimeNode
        PageType.Comments -> EditCommentsNode
    }
