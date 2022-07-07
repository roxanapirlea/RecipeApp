package com.roxana.recipeapp.edit

import androidx.annotation.StringRes
import com.roxana.recipeapp.R

data class Page(val type: PageType, @StringRes val name: Int, val isSelected: Boolean = false)

enum class PageType {
    Title,
    Photo,
    Categories,
    Portions,
    Ingredients,
    Instructions,
    Temperature,
    Time,
    Comments,
    Recap
}

private val addPages = listOf(
    Page(PageType.Title, R.string.edit_recipe_title_page),
    Page(PageType.Photo, R.string.edit_recipe_photo_page),
    Page(PageType.Categories, R.string.edit_recipe_categories_page),
    Page(PageType.Portions, R.string.edit_recipe_portions_page),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_page),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_page),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_page),
    Page(PageType.Time, R.string.edit_recipe_time_page),
    Page(PageType.Recap, R.string.edit_recipe_recap_page)
)

private val editPages = listOf(
    Page(PageType.Title, R.string.edit_recipe_title_page),
    Page(PageType.Photo, R.string.edit_recipe_photo_page),
    Page(PageType.Categories, R.string.edit_recipe_categories_page),
    Page(PageType.Portions, R.string.edit_recipe_portions_page),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_page),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_page),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_page),
    Page(PageType.Time, R.string.edit_recipe_time_page),
    Page(PageType.Comments, R.string.edit_recipe_comments_page),
    Page(PageType.Recap, R.string.edit_recipe_recap_page)
)

fun pagesForAddRecipe(selected: PageType): List<Page> = addPages.map {
    if (it.type == selected) it.copy(isSelected = true) else it
}

fun pagesForEditRecipe(selected: PageType): List<Page> = editPages.map {
    if (it.type == selected) it.copy(isSelected = true) else it
}
