package com.roxana.recipeapp

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Node {
    val route: String
    val args: List<NamedNavArgument>
}

interface CommonNode : Node

object HomeNode : CommonNode {
    private const val ROOT = "home"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object PhotoCaptureNode : CommonNode {
    private const val ROOT = "scan"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object SettingsNode : CommonNode {
    private const val ROOT = "settings"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

interface DetailNode : Node

object RecipeDetailNode : DetailNode {
    private const val ROOT = "detail"
    const val KEY_ID = "id"

    override val route: String = "$ROOT/{$KEY_ID}"
    override val args: List<NamedNavArgument> =
        listOf(navArgument(KEY_ID) { type = NavType.IntType })

    fun destination(id: Int) = "$ROOT/$id"
}

object CookingNode : DetailNode {
    private const val ROOT = "cooking"
    const val KEY_ID = "id"
    const val KEY_PORTIONS_MULTIPLIER = "portions_multiplier"

    override val route: String =
        "$ROOT/{$KEY_ID}?$KEY_PORTIONS_MULTIPLIER={$KEY_PORTIONS_MULTIPLIER}"
    override val args: List<NamedNavArgument> =
        listOf(
            navArgument(KEY_ID) { type = NavType.IntType },
            navArgument(KEY_PORTIONS_MULTIPLIER) { type = NavType.StringType }
        )

    fun destination(id: Int, portionsMultiplier: String = "1"): String =
        "$ROOT/$id?$KEY_PORTIONS_MULTIPLIER=$portionsMultiplier"
}

object AddCommentNode : DetailNode {
    private const val ROOT = "add_comment"
    const val KEY_ID = "id"

    override val route: String = "$ROOT/{$KEY_ID}"
    override val args: List<NamedNavArgument> =
        listOf(navArgument(KEY_ID) { type = NavType.IntType })

    fun destination(id: Int): String = "$ROOT/$id"
}

object VaryIngredientQuantitiesNode : DetailNode {
    private const val ROOT = "ingredient_quantities"
    const val KEY_RECIPE_ID = "recipe_id"

    override val route: String = "$ROOT/{$KEY_RECIPE_ID}"
    override val args: List<NamedNavArgument> =
        listOf(navArgument(KEY_RECIPE_ID) { type = NavType.IntType })

    fun destination(id: Int): String = "$ROOT/$id"
}
