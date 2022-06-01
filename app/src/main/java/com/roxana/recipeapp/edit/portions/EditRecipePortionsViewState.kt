package com.roxana.recipeapp.edit.portions

data class EditRecipePortionsViewState(
    val portions: String = ""
)

fun EditRecipePortionsViewState.isValid() = portions.isEmpty() || portions.toShortOrNull() != null
