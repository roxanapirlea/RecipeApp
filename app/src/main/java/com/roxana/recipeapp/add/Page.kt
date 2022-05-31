package com.roxana.recipeapp.add

import androidx.annotation.StringRes
import com.roxana.recipeapp.R

data class Page(val type: PageType, @StringRes val name: Int, val isSelected: Boolean = false)

enum class PageType {
    Title,
    Categories,
    Portions,
    Ingredients,
    Instructions,
    Temperature,
    Time
}

private val pages = listOf(
    Page(PageType.Title, R.string.add_recipe_title_page),
    Page(PageType.Categories, R.string.add_recipe_categories_page),
    Page(PageType.Portions, R.string.add_recipe_portions_page),
    Page(PageType.Ingredients, R.string.add_recipe_ingredients_page),
    Page(PageType.Instructions, R.string.add_recipe_instructions_page),
    Page(PageType.Temperature, R.string.add_recipe_temperature_page),
    Page(PageType.Time, R.string.add_recipe_time_page)
)

fun pagesForAddRecipe(selected: PageType): List<Page> = pages.map {
    if (it.type == selected) it.copy(isSelected = true) else it
}
