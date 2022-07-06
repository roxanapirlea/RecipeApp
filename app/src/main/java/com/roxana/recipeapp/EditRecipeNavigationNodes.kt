package com.roxana.recipeapp

import androidx.navigation.NamedNavArgument

interface EditNode : Node

object EditGraphRootNode : EditNode {
    private const val ROOT = "edit_recipe"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTitleNode : EditNode {
    private const val ROOT = "edit_title"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditChoosePhotoNode : EditNode {
    private const val ROOT = "edit_choose_photo"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditCategoriesNode : EditNode {
    private const val ROOT = "edit_categories"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditPortionsNode : EditNode {
    private const val ROOT = "edit_portions"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditIngredientsNode : EditNode {
    private const val ROOT = "edit_ingredients"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditInstructionsNode : EditNode {
    private const val ROOT = "edit_instructions"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTemperatureNode : EditNode {
    private const val ROOT = "edit_temperature"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditTimeNode : EditNode {
    private const val ROOT = "edit_time"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditCommentsNode : EditNode {
    private const val ROOT = "edit_comments"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}

object EditRecipeRecapNode : EditNode {
    private const val ROOT = "edit_recipe_recap"

    override val route: String = ROOT
    override val args: List<NamedNavArgument> = emptyList()
}
