package com.roxana.recipeapp.edit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.roxana.recipeapp.R

data class Page(
    val type: PageType,
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    val isCompleted: Boolean = false,
    val isSelected: Boolean = false,
    val hasConnectorEnd: Boolean = true,
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
    Page(PageType.Title, R.string.edit_recipe_title_page, R.drawable.ic_title),
    Page(PageType.Photo, R.string.edit_recipe_photo_page, R.drawable.ic_photo),
    Page(PageType.Categories, R.string.edit_recipe_categories_page, R.drawable.ic_categories),
    Page(PageType.Portions, R.string.edit_recipe_portions_page, R.drawable.ic_portions),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_page, R.drawable.ic_ingredients),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_page, R.drawable.ic_instructions),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_page, R.drawable.ic_temperature),
    Page(PageType.Time, R.string.edit_recipe_time_page, R.drawable.ic_time),
    Page(PageType.Recap, R.string.edit_recipe_recap_page, R.drawable.ic_recap, hasConnectorEnd = false),
)

private val editPages = listOf(
    Page(PageType.Title, R.string.edit_recipe_title_title, R.drawable.ic_title),
    Page(PageType.Photo, R.string.edit_recipe_photo_title, R.drawable.ic_photo),
    Page(PageType.Categories, R.string.edit_recipe_categories_title, R.drawable.ic_categories),
    Page(PageType.Portions, R.string.edit_recipe_portions_title, R.drawable.ic_portions),
    Page(PageType.Ingredients, R.string.edit_recipe_ingredients_title, R.drawable.ic_ingredients),
    Page(PageType.Instructions, R.string.edit_recipe_instructions_title, R.drawable.ic_instructions),
    Page(PageType.Temperature, R.string.edit_recipe_temperature_title, R.drawable.ic_temperature),
    Page(PageType.Time, R.string.edit_recipe_time_title, R.drawable.ic_time),
    Page(PageType.Comments, R.string.edit_recipe_comments_title, R.drawable.ic_comment),
    Page(PageType.Recap, R.string.edit_recipe_recap_title, R.drawable.ic_recap, hasConnectorEnd = false)
)

fun pagesForAddRecipe(selected: PageType): List<Page> {
    val selectedIndex = addPages.indexOfFirst { it.type == selected }
    return addPages.mapIndexed { index, page ->
        when {
            index == selectedIndex -> page.copy(isSelected = true)
            index < selectedIndex -> page.copy(isCompleted = true)
            else -> page
        }
    }
}

fun pagesForEditRecipe(selected: PageType): List<Page> {
    val selectedIndex = editPages.indexOfFirst { it.type == selected }
    return editPages.mapIndexed { index, page ->
        when {
            index == selectedIndex -> page.copy(isSelected = true)
            index < selectedIndex -> page.copy(isCompleted = true)
            else -> page
        }
    }
}
