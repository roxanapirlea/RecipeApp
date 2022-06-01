package com.roxana.recipeapp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesScreen
import com.roxana.recipeapp.edit.categories.EditRecipeCategoriesViewModel
import com.roxana.recipeapp.edit.ingredients.EditRecipeIngredientsScreen
import com.roxana.recipeapp.edit.ingredients.EditRecipeIngredientsViewModel
import com.roxana.recipeapp.edit.instructions.EditRecipeInstructionsScreen
import com.roxana.recipeapp.edit.instructions.EditRecipeInstructionsViewModel
import com.roxana.recipeapp.edit.portions.EditRecipePortionsScreen
import com.roxana.recipeapp.edit.portions.EditRecipePortionsViewModel
import com.roxana.recipeapp.edit.recap.RecapScreen
import com.roxana.recipeapp.edit.recap.RecapViewModel
import com.roxana.recipeapp.edit.temperature.EditRecipeTemperatureScreen
import com.roxana.recipeapp.edit.temperature.EditRecipeTemperatureViewModel
import com.roxana.recipeapp.edit.time.EditRecipeTimeScreen
import com.roxana.recipeapp.edit.time.EditRecipeTimeViewModel
import com.roxana.recipeapp.edit.title.EditRecipeTitleScreen
import com.roxana.recipeapp.edit.title.EditRecipeTitleViewModel

fun NavGraphBuilder.editRecipeGraph(navController: NavController) {
    navigation(
        startDestination = EditRecipeTitle.route,
        route = EditRecipeGraphRootScreen.route
    ) {

        composable(route = EditRecipeTitle.route) {
            val editRecipeTitleViewModel = hiltViewModel<EditRecipeTitleViewModel>()
            EditRecipeTitleScreen(
                editRecipeTitleViewModel = editRecipeTitleViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeCategories.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeTitle, it.toScreen()) },
            )
        }
        composable(route = EditRecipeCategories.route) {
            val editRecipeCategoriesViewModel = hiltViewModel<EditRecipeCategoriesViewModel>()
            EditRecipeCategoriesScreen(
                editRecipeCategoriesViewModel = editRecipeCategoriesViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipePortions.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeCategories, it.toScreen()) },
            )
        }
        composable(route = EditRecipePortions.route) {
            val editRecipePortionsViewModel = hiltViewModel<EditRecipePortionsViewModel>()
            EditRecipePortionsScreen(
                editRecipePortionsViewModel = editRecipePortionsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeIngredients.route) },
                onNavigate = { navController.popOrNavigate(EditRecipePortions, it.toScreen()) },
            )
        }
        composable(route = EditRecipeIngredients.route) {
            val editRecipeIngredientsViewModel = hiltViewModel<EditRecipeIngredientsViewModel>()
            EditRecipeIngredientsScreen(
                ingredientsViewModel = editRecipeIngredientsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeInstructions.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeIngredients, it.toScreen()) },
            )
        }
        composable(route = EditRecipeInstructions.route) {
            val instructionsViewModel = hiltViewModel<EditRecipeInstructionsViewModel>()
            EditRecipeInstructionsScreen(
                instructionsViewModel = instructionsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeTemperature.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeInstructions, it.toScreen()) },
            )
        }
        composable(route = EditRecipeTemperature.route) {
            val editRecipeTemperatureViewModel = hiltViewModel<EditRecipeTemperatureViewModel>()
            EditRecipeTemperatureScreen(
                editRecipeTemperatureViewModel = editRecipeTemperatureViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeTime.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeTemperature, it.toScreen()) },
            )
        }
        composable(route = EditRecipeTime.route) {
            val editRecipeTimeViewModel = hiltViewModel<EditRecipeTimeViewModel>()
            EditRecipeTimeScreen(
                editRecipeTimeViewModel = editRecipeTimeViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(EditRecipeRecap.route) },
                onNavigate = { navController.popOrNavigate(EditRecipeTime, it.toScreen()) },
            )
        }
        composable(route = EditRecipeRecap.route) {
            val recapViewModel = hiltViewModel<RecapViewModel>()
            RecapScreen(
                recapViewModel = recapViewModel,
                onBack = { navController.navigateUp() },
                onFinish = { navController.popBackStack(Home.route, false) }
            )
        }
    }
}

private fun NavController.popOrNavigate(from: EditRecipeScreen, to: EditRecipeScreen) {
    if (from.position <= to.position)
        navigate(to.route)
    else {
        val wasPopped = popBackStack(to.route, false)
        if (!wasPopped) {
            popBackStack(EditRecipeTitle.route, true)
            navigate(to.route)
        }
    }
}

private fun PageType.toScreen() =
    when (this) {
        PageType.Title -> EditRecipeTitle
        PageType.Categories -> EditRecipeCategories
        PageType.Portions -> EditRecipePortions
        PageType.Ingredients -> EditRecipeIngredients
        PageType.Instructions -> EditRecipeInstructions
        PageType.Temperature -> EditRecipeTemperature
        PageType.Time -> EditRecipeTime
    }

interface EditRecipeScreen : Screen {
    val position: Short
}

object EditRecipeGraphRootScreen : EditRecipeScreen {
    private const val rootRoute = "editRecipeRoot"

    override val position: Short = 0
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeTitle : EditRecipeScreen {
    private const val rootRoute = "editTitle"

    override val position: Short = 1
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeCategories : EditRecipeScreen {
    private const val rootRoute = "editCategories"

    override val position: Short = 2
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipePortions : EditRecipeScreen {
    private const val rootRoute = "editPortions"

    override val position: Short = 3
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeIngredients : EditRecipeScreen {
    private const val rootRoute = "editIngredients"

    override val position: Short = 4
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeInstructions : EditRecipeScreen {
    private const val rootRoute = "editInstructions"

    override val position: Short = 5
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeTemperature : EditRecipeScreen {
    private const val rootRoute = "editTemperature"

    override val position: Short = 6
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeTime : EditRecipeScreen {
    private const val rootRoute = "editTime"

    override val position: Short = 7
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object EditRecipeRecap : CommonScreen {
    private const val rootRoute = "editRecipeRecap"

    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}
