package com.roxana.recipeapp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.roxana.recipeapp.add.PageType
import com.roxana.recipeapp.add.categories.AddRecipeCategoriesScreen
import com.roxana.recipeapp.add.categories.AddRecipeCategoriesViewModel
import com.roxana.recipeapp.add.ingredients.AddRecipeIngredientsScreen
import com.roxana.recipeapp.add.ingredients.AddRecipeIngredientsViewModel
import com.roxana.recipeapp.add.instructions.AddRecipeInstructionsScreen
import com.roxana.recipeapp.add.instructions.AddRecipeInstructionsViewModel
import com.roxana.recipeapp.add.portions.AddRecipePortionsScreen
import com.roxana.recipeapp.add.portions.AddRecipePortionsViewModel
import com.roxana.recipeapp.add.temperature.AddRecipeTemperatureScreen
import com.roxana.recipeapp.add.temperature.AddRecipeTemperatureViewModel
import com.roxana.recipeapp.add.time.AddRecipeTimeScreen
import com.roxana.recipeapp.add.time.AddRecipeTimeViewModel
import com.roxana.recipeapp.add.title.AddRecipeTitleScreen
import com.roxana.recipeapp.add.title.AddRecipeTitleViewModel

fun NavGraphBuilder.addRecipeGraph(navController: NavController) {
    navigation(
        startDestination = AddRecipeTitle.route,
        route = AddRecipeGraphRootScreen.route
    ) {

        composable(route = AddRecipeTitle.route) {
            val addRecipeTitleViewModel = hiltViewModel<AddRecipeTitleViewModel>()
            AddRecipeTitleScreen(
                addRecipeTitleViewModel = addRecipeTitleViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipeCategories.route) },
                onNavigate = { navController.popOrNavigate(AddRecipeTitle, it.toScreen()) },
            )
        }
        composable(route = AddRecipeCategories.route) {
            val addRecipeCategoriesViewModel = hiltViewModel<AddRecipeCategoriesViewModel>()
            AddRecipeCategoriesScreen(
                addRecipeCategoriesViewModel = addRecipeCategoriesViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipePortions.route) },
                onNavigate = { navController.popOrNavigate(AddRecipeCategories, it.toScreen()) },
            )
        }
        composable(route = AddRecipePortions.route) {
            val addRecipePortionsViewModel = hiltViewModel<AddRecipePortionsViewModel>()
            AddRecipePortionsScreen(
                addRecipePortionsViewModel = addRecipePortionsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipeIngredients.route) },
                onNavigate = { navController.popOrNavigate(AddRecipePortions, it.toScreen()) },
            )
        }
        composable(route = AddRecipeIngredients.route) {
            val addRecipeIngredientsViewModel = hiltViewModel<AddRecipeIngredientsViewModel>()
            AddRecipeIngredientsScreen(
                ingredientsViewModel = addRecipeIngredientsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipeInstructions.route) },
                onNavigate = { navController.popOrNavigate(AddRecipeIngredients, it.toScreen()) },
            )
        }
        composable(route = AddRecipeInstructions.route) {
            val instructionsViewModel = hiltViewModel<AddRecipeInstructionsViewModel>()
            AddRecipeInstructionsScreen(
                instructionsViewModel = instructionsViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipeTemperature.route) },
                onNavigate = { navController.popOrNavigate(AddRecipeInstructions, it.toScreen()) },
            )
        }
        composable(route = AddRecipeTemperature.route) {
            val addRecipeTemperatureViewModel = hiltViewModel<AddRecipeTemperatureViewModel>()
            AddRecipeTemperatureScreen(
                addRecipeTemperatureViewModel = addRecipeTemperatureViewModel,
                onBack = { navController.navigateUp() },
                onForward = { navController.navigate(AddRecipeTime.route) },
                onNavigate = { navController.popOrNavigate(AddRecipeTemperature, it.toScreen()) },
            )
        }
        composable(route = AddRecipeTime.route) {
            val addRecipeTimeViewModel = hiltViewModel<AddRecipeTimeViewModel>()
            AddRecipeTimeScreen(
                addRecipeTimeViewModel = addRecipeTimeViewModel,
                onBack = { navController.navigateUp() },
                onForward = { TODO() },
                onNavigate = { navController.popOrNavigate(AddRecipeTime, it.toScreen()) },
            )
        }
    }
}

private fun NavController.popOrNavigate(from: AddRecipeScreen, to: AddRecipeScreen) {
    if (from.position <= to.position)
        navigate(to.route)
    else {
        val wasPopped = popBackStack(to.route, false)
        if (!wasPopped) {
            popBackStack(AddRecipeTitle.route, true)
            navigate(to.route)
        }
    }
}

private fun PageType.toScreen() =
    when (this) {
        PageType.Title -> AddRecipeTitle
        PageType.Categories -> AddRecipeCategories
        PageType.Portions -> AddRecipePortions
        PageType.Ingredients -> AddRecipeIngredients
        PageType.Instructions -> AddRecipeInstructions
        PageType.Temperature -> AddRecipeTemperature
        PageType.Time -> AddRecipeTime
    }

interface AddRecipeScreen : Screen {
    val position: Short
}

object AddRecipeGraphRootScreen : AddRecipeScreen {
    private const val rootRoute = "addRecipeRoot"

    override val position: Short = 0
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeTitle : AddRecipeScreen {
    private const val rootRoute = "addTitle"

    override val position: Short = 1
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeCategories : AddRecipeScreen {
    private const val rootRoute = "addCategories"

    override val position: Short = 2
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipePortions : AddRecipeScreen {
    private const val rootRoute = "addPortions"

    override val position: Short = 3
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeIngredients : AddRecipeScreen {
    private const val rootRoute = "addIngredients"

    override val position: Short = 4
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeInstructions : AddRecipeScreen {
    private const val rootRoute = "addInstructions"

    override val position: Short = 5
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeTemperature : AddRecipeScreen {
    private const val rootRoute = "addTemperature"

    override val position: Short = 6
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object AddRecipeTime : AddRecipeScreen {
    private const val rootRoute = "addTime"

    override val position: Short = 7
    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}
