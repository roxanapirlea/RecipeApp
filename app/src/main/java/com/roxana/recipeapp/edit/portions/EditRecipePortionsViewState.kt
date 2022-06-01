package com.roxana.recipeapp.edit.portions

data class EditRecipePortionsViewState(
    val portions: String = "",
    val isExistingRecipe: Boolean = false,
)

fun EditRecipePortionsViewState.isValid() = portions.isEmpty() || portions.toShortOrNull() != null
