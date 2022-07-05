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
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = { navController.switch(EditTitleNode, EditCategoriesNode) },
                onEditNavForward = { navController.switch(EditTitleNode, EditCategoriesNode) },
                onNavToPage = { navController.switch(EditTitleNode, it.toNode()) },
            )
        }
        composable(route = EditCategoriesNode.route) {
            val editRecipeCategoriesViewModel = hiltViewModel<EditRecipeCategoriesViewModel>()
            EditRecipeCategoriesDestination(
                editRecipeCategoriesViewModel = editRecipeCategoriesViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = {
                    navController.switch(EditCategoriesNode, EditPortionsNode)
                },
                onEditNavForward = { navController.switch(EditCategoriesNode, EditPortionsNode) },
                onNavToPage = { navController.switch(EditCategoriesNode, it.toNode()) },
            )
        }
        composable(route = EditPortionsNode.route) {
            val editRecipePortionsViewModel = hiltViewModel<EditRecipePortionsViewModel>()
            EditRecipePortionsDestination(
                editRecipePortionsViewModel = editRecipePortionsViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = {
                    navController.switch(EditPortionsNode, EditIngredientsNode)
                },
                onEditNavForward = { navController.switch(EditPortionsNode, EditIngredientsNode) },
                onNavToPage = { navController.switch(EditPortionsNode, it.toNode()) },
            )
        }
        composable(route = EditIngredientsNode.route) {
            val editRecipeIngredientsViewModel = hiltViewModel<EditRecipeIngredientsViewModel>()
            EditRecipeIngredientsDestination(
                ingredientsViewModel = editRecipeIngredientsViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = {
                    navController.switch(EditIngredientsNode, EditInstructionsNode)
                },
                onEditNavForward = {
                    navController.switch(EditIngredientsNode, EditInstructionsNode)
                },
                onNavToPage = { navController.switch(EditIngredientsNode, it.toNode()) },
            )
        }
        composable(route = EditInstructionsNode.route) {
            val instructionsViewModel = hiltViewModel<EditRecipeInstructionsViewModel>()
            EditRecipeInstructionsDestination(
                instructionsViewModel = instructionsViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = {
                    navController.switch(EditInstructionsNode, EditTemperatureNode)
                },
                onEditNavForward = {
                    navController.switch(EditInstructionsNode, EditTemperatureNode)
                },
                onNavToPage = { navController.switch(EditInstructionsNode, it.toNode()) },
            )
        }
        composable(route = EditTemperatureNode.route) {
            val editRecipeTemperatureViewModel = hiltViewModel<EditRecipeTemperatureViewModel>()
            EditRecipeTemperatureDestination(
                editRecipeTemperatureViewModel = editRecipeTemperatureViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = { navController.switch(EditTemperatureNode, EditTimeNode) },
                onEditNavForward = { navController.switch(EditTemperatureNode, EditTimeNode) },
                onNavToPage = { navController.switch(EditTemperatureNode, it.toNode()) },
            )
        }
        composable(route = EditTimeNode.route) {
            val editRecipeTimeViewModel = hiltViewModel<EditRecipeTimeViewModel>()
            EditRecipeTimeDestination(
                editRecipeTimeViewModel = editRecipeTimeViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = { navController.switch(EditTimeNode, EditRecipeRecapNode) },
                onEditNavForward = { navController.switch(EditTimeNode, EditCommentsNode) },
                onNavToPage = { navController.switch(EditTimeNode, it.toNode()) },
            )
        }
        composable(route = EditCommentsNode.route) {
            val commentsViewModel = hiltViewModel<EditRecipeCommentsViewModel>()
            EditRecipeCommentsDestination(
                commentsViewModel = commentsViewModel,
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onCreationNavForward = {
                    navController.switch(EditCommentsNode, EditRecipeRecapNode)
                },
                onEditNavForward = { navController.switch(EditCommentsNode, EditRecipeRecapNode) },
                onNavToPage = { navController.switch(EditCommentsNode, it.toNode()) },
            )
        }
        composable(route = EditRecipeRecapNode.route) {
            val recapViewModel = hiltViewModel<RecapViewModel>()
            RecapDestination(
                recapViewModel = recapViewModel,
                onNavBack = { navController.switch(EditRecipeRecapNode, EditTitleNode) },
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onNavToPage = { navController.switch(EditRecipeRecapNode, it.toNode()) },
            )
        }
    }
}

private fun NavController.switch(currentNode: EditNode, nextNode: EditNode) {
    if (currentNode.ordinal() == nextNode.ordinal()) return
    if (currentNode.ordinal() > nextNode.ordinal())
        navigate(nextNode.route)
    else
        navigate(nextNode.route) { popUpTo(EditTitleNode.route) }
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

private fun EditNode.ordinal() = when (this) {
    EditTitleNode -> 0
    EditCategoriesNode -> 1
    EditPortionsNode -> 2
    EditIngredientsNode -> 3
    EditInstructionsNode -> 4
    EditTemperatureNode -> 5
    EditTimeNode -> 6
    EditCommentsNode -> 7
    EditRecipeRecapNode -> 8
    else -> Int.MAX_VALUE
}
