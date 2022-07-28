package com.roxana.recipeapp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesDestination
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesViewModel
import com.roxana.recipeapp.edit.choosephoto.EditRecipeChoosePhotoDestination
import com.roxana.recipeapp.edit.choosephoto.EditRecipeChoosePhotoViewModel
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
                onCreationNavForward = { navController.navigate(EditChoosePhotoNode.route) },
                onEditNavForward = { navController.navigate(EditChoosePhotoNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditTitleNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditChoosePhotoNode.route) {
            val editRecipeChoosePhotoViewModel = hiltViewModel<EditRecipeChoosePhotoViewModel>()
            EditRecipeChoosePhotoDestination(
                editRecipeChoosePhotoViewModel = editRecipeChoosePhotoViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditCategoriesNode.route) },
                onEditNavForward = { navController.navigate(EditCategoriesNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditChoosePhotoNode, page.toNode(), isEditing)
                },
                onCapturePhoto = { navController.navigate(PhotoCaptureNode.route) }
            )
        }
        composable(route = EditCategoriesNode.route) {
            val editRecipeCategoriesViewModel = hiltViewModel<EditRecipeCategoriesViewModel>()
            EditRecipeCategoriesDestination(
                editRecipeCategoriesViewModel = editRecipeCategoriesViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditPortionsNode.route) },
                onEditNavForward = { navController.navigate(EditPortionsNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditCategoriesNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditPortionsNode.route) {
            val editRecipePortionsViewModel = hiltViewModel<EditRecipePortionsViewModel>()
            EditRecipePortionsDestination(
                editRecipePortionsViewModel = editRecipePortionsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditIngredientsNode.route) },
                onEditNavForward = { navController.navigate(EditIngredientsNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditPortionsNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditIngredientsNode.route) {
            val editRecipeIngredientsViewModel = hiltViewModel<EditRecipeIngredientsViewModel>()
            EditRecipeIngredientsDestination(
                ingredientsViewModel = editRecipeIngredientsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditInstructionsNode.route) },
                onEditNavForward = { navController.navigate(EditInstructionsNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditIngredientsNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditInstructionsNode.route) {
            val instructionsViewModel = hiltViewModel<EditRecipeInstructionsViewModel>()
            EditRecipeInstructionsDestination(
                instructionsViewModel = instructionsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditTemperatureNode.route) },
                onEditNavForward = { navController.navigate(EditTemperatureNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditInstructionsNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditTemperatureNode.route) {
            val editRecipeTemperatureViewModel = hiltViewModel<EditRecipeTemperatureViewModel>()
            EditRecipeTemperatureDestination(
                editRecipeTemperatureViewModel = editRecipeTemperatureViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditTimeNode.route) },
                onEditNavForward = { navController.navigate(EditTimeNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditTemperatureNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditTimeNode.route) {
            val editRecipeTimeViewModel = hiltViewModel<EditRecipeTimeViewModel>()
            EditRecipeTimeDestination(
                editRecipeTimeViewModel = editRecipeTimeViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.navigate(EditCommentsNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditTimeNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditCommentsNode.route) {
            val commentsViewModel = hiltViewModel<EditRecipeCommentsViewModel>()
            EditRecipeCommentsDestination(
                commentsViewModel = commentsViewModel,
                onNavBack = { navController.navigateUp() },
                onCreationNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onEditNavForward = { navController.navigate(EditRecipeRecapNode.route) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditCommentsNode, page.toNode(), isEditing)
                },
            )
        }
        composable(route = EditRecipeRecapNode.route) {
            val recapViewModel = hiltViewModel<RecapViewModel>()
            RecapDestination(
                recapViewModel = recapViewModel,
                onNavBack = { navController.navigateUp() },
                onNavFinish = { navController.popBackStack(HomeNode.route, false) },
                onNavToPage = { page, isEditing ->
                    navController.fromNodeToNode(EditRecipeRecapNode, page.toNode(), isEditing)
                },
            )
        }
    }
}

private fun NavController.fromNodeToNode(
    currentNode: EditNode,
    nextNode: EditNode,
    isEdit: Boolean
) {
    val nodes = if (isEdit) editingEditNodes else creationEditNodes
    val currentIndex = nodes.indexOf(currentNode)
    val nextIndex = nodes.indexOf(nextNode)
    if (currentIndex == nextIndex) return
    if (currentIndex < nextIndex)
        for (i in (currentIndex + 1)..nextIndex) navigate(nodes[i].route)
    else
        popBackStack(nextNode.route, inclusive = false)
}

private fun PageType.toNode() =
    when (this) {
        PageType.Title -> EditTitleNode
        PageType.Photo -> EditChoosePhotoNode
        PageType.Categories -> EditCategoriesNode
        PageType.Portions -> EditPortionsNode
        PageType.Ingredients -> EditIngredientsNode
        PageType.Instructions -> EditInstructionsNode
        PageType.Temperature -> EditTemperatureNode
        PageType.Time -> EditTimeNode
        PageType.Comments -> EditCommentsNode
        PageType.Recap -> EditRecipeRecapNode
    }

private val creationEditNodes = listOf(
    EditTitleNode,
    EditChoosePhotoNode,
    EditCategoriesNode,
    EditPortionsNode,
    EditIngredientsNode,
    EditInstructionsNode,
    EditTemperatureNode,
    EditTimeNode,
    EditRecipeRecapNode,
)

private val editingEditNodes = listOf(
    EditTitleNode,
    EditChoosePhotoNode,
    EditCategoriesNode,
    EditPortionsNode,
    EditIngredientsNode,
    EditInstructionsNode,
    EditTemperatureNode,
    EditTimeNode,
    EditCommentsNode,
    EditRecipeRecapNode,
)
