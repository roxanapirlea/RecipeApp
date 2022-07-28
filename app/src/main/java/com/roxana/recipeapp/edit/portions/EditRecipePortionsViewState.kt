package com.roxana.recipeapp.edit.portions

import com.roxana.recipeapp.edit.PageType

data class EditRecipePortionsViewState(
    val portions: String = "",
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}

fun EditRecipePortionsViewState.isValid() = portions.isEmpty() || portions.toShortOrNull() != null
