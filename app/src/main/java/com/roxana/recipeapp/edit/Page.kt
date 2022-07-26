package com.roxana.recipeapp.edit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.roxana.recipeapp.R

data class Page(
    val type: PageType,
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    val isSelected: Boolean = false,
    val hasDivider: Boolean = true
)

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
    Page(PageType.Title, R.string.edit_recipe_title_page, R.drawable.ic_title, hasDivider = false),
    Page(PageType.Photo, R.string.edit_recipe_photo_page, R.drawable.ic_photo),
    Page(PageType.Categories, R.string.edit_recipe_categories_page, R.drawable.ic_categories),
    Page(PageType.Portions, R.string.edit_recipe_portions_page, R.drawable.ic_portions),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_page, R.drawable.ic_ingredients),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_page, R.drawable.ic_instructions),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_page, R.drawable.ic_temperature),
    Page(PageType.Time, R.string.edit_recipe_time_page, R.drawable.ic_time),
    Page(PageType.Recap, R.string.edit_recipe_recap_page, R.drawable.ic_recap),
)

private val editPages = listOf(
    Page(PageType.Title, R.string.edit_recipe_title_page, R.drawable.ic_title, hasDivider = false),
    Page(PageType.Photo, R.string.edit_recipe_photo_page, R.drawable.ic_photo),
    Page(PageType.Categories, R.string.edit_recipe_categories_page, R.drawable.ic_categories),
    Page(PageType.Portions, R.string.edit_recipe_portions_page, R.drawable.ic_portions),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_page, R.drawable.ic_ingredients),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_page, R.drawable.ic_instructions),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_page, R.drawable.ic_temperature),
    Page(PageType.Time, R.string.edit_recipe_time_page, R.drawable.ic_time),
    Page(PageType.Comments, R.string.edit_recipe_comments_page, R.drawable.ic_comment),
    Page(PageType.Recap, R.string.edit_recipe_recap_page, R.drawable.ic_recap)
)

fun pagesForAddRecipe(selected: PageType): List<Page> = addPages.map {
    if (it.type == selected) it.copy(isSelected = true) else it
}

fun pagesForEditRecipe(selected: PageType): List<Page> = editPages.map {
    if (it.type == selected) it.copy(isSelected = true) else it
}
