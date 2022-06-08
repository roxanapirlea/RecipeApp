package com.roxana.recipeapp

import androidx.navigation.NamedNavArgument

interface EditNode : Node {
    val position: Short
}

object EditGraphRootNode : EditNode {
    private const val ROOT = "edit_recipe"

    override val position: Short = 0
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTitleNode : EditNode {
    private const val ROOT = "edit_title"

    override val position: Short = 1
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditCategoriesNode : EditNode {
    private const val ROOT = "edit_categories"

    override val position: Short = 2
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditPortionsNode : EditNode {
    private const val ROOT = "edit_portions"

    override val position: Short = 3
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditIngredientsNode : EditNode {
    private const val ROOT = "edit_ingredients"

    override val position: Short = 4
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditInstructionsNode : EditNode {
    private const val ROOT = "edit_instructions"

    override val position: Short = 5
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTemperatureNode : EditNode {
    private const val ROOT = "edit_temperature"

    override val position: Short = 6
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTimeNode : EditNode {
    private const val ROOT = "edit_time"

    override val position: Short = 7
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditCommentsNode : EditNode {
    private const val ROOT = "edit_comments"

    override val position: Short = 8
    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditRecipeRecapNode : CommonNode {
    private const val ROOT = "edit_recipe_recap"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}
