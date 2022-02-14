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
import com.roxana.recipeapp.add.portions.AddRecipePortionsScreen
import com.roxana.recipeapp.add.portions.AddRecipePortionsViewModel
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
                onForward = { TODO() },
                onNavigate = { navController.popOrNavigate(AddRecipePortions, it.toScreen()) },
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
        PageType.Ingredients -> TODO()
        PageType.Instructions -> TODO()
        PageType.Temperature -> TODO()
        PageType.Time -> TODO()
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
