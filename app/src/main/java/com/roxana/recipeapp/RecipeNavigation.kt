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
import com.roxana.recipeapp.settings.SettingsScreen
import com.roxana.recipeapp.settings.SettingsViewModel
import com.roxana.recipeapp.settings.debug.DebugSettingsScreen
import com.roxana.recipeapp.settings.debug.DebugSettingsViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RecipeNavigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, startDestination = Home.route) {
            editRecipeGraph(navController)

            composable(route = Home.route) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onNavDetail = {
                        navController.navigate(
                            RecipeDetail.destination(RecipeDetail.Arguments(it))
                        )
                    },
                    onNavAddRecipe = { navController.navigate(EditRecipeGraphRootScreen.route) },
                    onNavSettings = { navController.navigate(Settings.route) },
                )
            }
            composable(route = Settings.route) {
                val settingsViewModel = hiltViewModel<SettingsViewModel>()
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    onBack = { navController.navigateUp() },
                    onDebugSettings = { navController.navigate(DebugSettings.route) }
                )
            }
            composable(route = DebugSettings.route) {
                val debugSettingsViewModel = hiltViewModel<DebugSettingsViewModel>()
                DebugSettingsScreen(
                    debugSettingsViewModel = debugSettingsViewModel,
                    onBack = { navController.navigateUp() }
                )
            }
            composable(
                route = RecipeDetail.route,
                arguments = RecipeDetail.arguments
            ) { backStackEntry ->
                val detailViewModel = hiltViewModel<DetailViewModel>()
                DetailScreen(
                    detailViewModel = detailViewModel,
                    onStartCooking = {
                        val recipeId = backStackEntry.arguments!!.getInt(RecipeDetail.KEY_ID)
                        navController.navigate(Cooking.destination(Cooking.Arguments(recipeId)))
                    },
                    onBack = { navController.navigateUp() },
                    onAddComment = {
                        val recipeId = backStackEntry.arguments!!.getInt(RecipeDetail.KEY_ID)
                        navController.navigate(AddComment.destination(AddComment.Arguments(recipeId)))
                    },
                    onEdit = { navController.navigate(EditRecipeGraphRootScreen.route) }
                )
            }
            composable(
                route = Cooking.route,
                arguments = Cooking.arguments
            ) { backStackEntry ->
                val cookingViewModel = hiltViewModel<CookingViewModel>()
                CookingScreen(
                    cookingViewModel = cookingViewModel,
                    onBack = { navController.navigateUp() },
                    onAddComment = {
                        val recipeId = backStackEntry.arguments!!.getInt(Cooking.KEY_ID)
                        navController.navigate(AddComment.destination(AddComment.Arguments(recipeId)))
                    },
                    onVaryIngredient = {
                        val recipeId = backStackEntry.arguments!!.getInt(Cooking.KEY_ID)
                        navController.navigate(
                            VaryIngredientQuantities.destination(
                                VaryIngredientQuantities.Arguments(recipeId)
                            )
                        )
                    }
                )
            }
            bottomSheet(
                route = AddComment.route,
                arguments = AddComment.arguments
            ) {
                val addCommentViewModel = hiltViewModel<AddCommentViewModel>()
                AddCommentScreen(
                    addCommentViewModel = addCommentViewModel,
                    onBack = { navController.navigateUp() }
                )
            }
            bottomSheet(
                route = VaryIngredientQuantities.route,
                arguments = VaryIngredientQuantities.arguments
            ) {
                val varyIngredientsViewModel = hiltViewModel<VaryIngredientsViewModel>()
                VaryIngredientsScreen(
                    varyIngredientsViewModel = varyIngredientsViewModel
                ) { quantityMultiplier, recipeId ->
                    navController.popBackStack(Cooking.route, true)
                    navController.navigate(
                        Cooking.destination(
                            Cooking.Arguments(recipeId, quantityMultiplier.toString())
                        )
                    )
                }
            }
        }
    }
}

interface Screen {
    val route: String
    val arguments: List<NamedNavArgument>
}

interface CommonScreen : Screen

object Home : CommonScreen {
    private const val rootRoute = "home"

    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object Settings : CommonScreen {
    private const val rootRoute = "settings"

    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

object DebugSettings : CommonScreen {
    private const val rootRoute = "debug_settings"

    override val route: String = rootRoute
    override val arguments: List<NamedNavArgument> = emptyList()
}

interface DetailScreen : Screen

object RecipeDetail : DetailScreen {
    private const val ROOT_ROUTE = "detail"
    const val KEY_ID = "id"

    override val route: String = "$ROOT_ROUTE/{$KEY_ID}"
    override val arguments: List<NamedNavArgument> =
        listOf(navArgument(KEY_ID) { type = NavType.IntType })

    fun destination(args: Arguments) = "$ROOT_ROUTE/${args.id}"

    data class Arguments(val id: Int)
}

object Cooking : DetailScreen {
    private const val ROOT_ROUTE = "cooking"
    const val KEY_ID = "id"
    const val KEY_PORTIONS_MULTIPLIER = "portions_multiplier"

    override val route: String =
        "$ROOT_ROUTE/{$KEY_ID}?$KEY_PORTIONS_MULTIPLIER={$KEY_PORTIONS_MULTIPLIER}"
    override val arguments: List<NamedNavArgument> =
        listOf(
            navArgument(KEY_ID) { type = NavType.IntType },
            navArgument(KEY_PORTIONS_MULTIPLIER) { type = NavType.StringType }
        )

    fun destination(arguments: Arguments): String =
        "$ROOT_ROUTE/${arguments.id}?$KEY_PORTIONS_MULTIPLIER=${arguments.portionsMultiplier}"

    data class Arguments(val id: Int, val portionsMultiplier: String = "1")
}

object AddComment : DetailScreen {
    private const val ROOT_ROUTE = "add_comment"
    const val KEY_ID = "id"

    override val route: String = "$ROOT_ROUTE/{$KEY_ID}"
    override val arguments: List<NamedNavArgument> =
        listOf(navArgument(KEY_ID) { type = NavType.IntType })

    fun destination(arguments: Arguments): String = "$ROOT_ROUTE/${arguments.id}"

    data class Arguments(val id: Int)
}

object VaryIngredientQuantities : DetailScreen {
    private const val ROOT_ROUTE = "ingredient_quantities"
    const val KEY_RECIPE_ID = "recipe_id"

    override val route: String = "$ROOT_ROUTE/{$KEY_RECIPE_ID}"
    override val arguments: List<NamedNavArgument> =
        listOf(navArgument(KEY_RECIPE_ID) { type = NavType.IntType })

    fun destination(arguments: Arguments): String = "$ROOT_ROUTE/${arguments.id}"

    data class Arguments(val id: Int)
}
