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
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditCategoriesNode.route) },
                onEditNavForward = { navController.switch(EditCategoriesNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditCategoriesNode.route) {
            val editRecipeCategoriesViewModel = hiltViewModel<EditRecipeCategoriesViewModel>()
            EditRecipeCategoriesDestination(
                editRecipeCategoriesViewModel = editRecipeCategoriesViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditPortionsNode.route) },
                onEditNavForward = { navController.switch(EditPortionsNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditPortionsNode.route) {
            val editRecipePortionsViewModel = hiltViewModel<EditRecipePortionsViewModel>()
            EditRecipePortionsDestination(
                editRecipePortionsViewModel = editRecipePortionsViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditIngredientsNode.route) },
                onEditNavForward = { navController.switch(EditIngredientsNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditIngredientsNode.route) {
            val editRecipeIngredientsViewModel = hiltViewModel<EditRecipeIngredientsViewModel>()
            EditRecipeIngredientsDestination(
                ingredientsViewModel = editRecipeIngredientsViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditInstructionsNode.route) },
                onEditNavForward = { navController.switch(EditInstructionsNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditInstructionsNode.route) {
            val instructionsViewModel = hiltViewModel<EditRecipeInstructionsViewModel>()
            EditRecipeInstructionsDestination(
                instructionsViewModel = instructionsViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditTemperatureNode.route) },
                onEditNavForward = { navController.switch(EditTemperatureNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditTemperatureNode.route) {
            val editRecipeTemperatureViewModel = hiltViewModel<EditRecipeTemperatureViewModel>()
            EditRecipeTemperatureDestination(
                editRecipeTemperatureViewModel = editRecipeTemperatureViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditTimeNode.route) },
                onEditNavForward = { navController.switch(EditTimeNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditTimeNode.route) {
            val editRecipeTimeViewModel = hiltViewModel<EditRecipeTimeViewModel>()
            EditRecipeTimeDestination(
                editRecipeTimeViewModel = editRecipeTimeViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.switch(EditCommentsNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditCommentsNode.route) {
            val commentsViewModel = hiltViewModel<EditRecipeCommentsViewModel>()
            EditRecipeCommentsDestination(
                commentsViewModel = commentsViewModel,
                onNavFinish = { navController.navigateUp() },
                onCreationNavForward = { navController.switch(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.switch(EditRecipeRecapNode.route) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
        composable(route = EditRecipeRecapNode.route) {
            val recapViewModel = hiltViewModel<RecapViewModel>()
            RecapDestination(
                recapViewModel = recapViewModel,
                onNavBack = { navController.switch(EditTitleNode.route) },
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onNavToPage = { navController.switch(it.toNode().route) },
            )
        }
    }
}

private fun NavController.switch(route: String) {
    navigate(route) { popUpTo(EditTitleNode.route) { inclusive = true } }
}

private fun PageType.toNode() =
    when (this) {
        PageType.Title -> EditTitleNode
        PageType.Categories -> EditCategoriesNode
        PageType.Portions -> EditPortionsNode
        PageType.Ingredients -> EditIngredientsNode
        PageType.Instructions -> EditInstructionsNode
        PageType.Temperature -> EditTemperatureNode
        PageType.Time -> EditTimeNode
        PageType.Comments -> EditCommentsNode
        PageType.Recap -> EditRecipeRecapNode
    }
