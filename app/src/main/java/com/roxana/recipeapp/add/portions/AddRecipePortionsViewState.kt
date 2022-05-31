package com.roxana.recipeapp.add.portions

data class AddRecipePortionsViewState(
    val portions: String = ""
)

fun AddRecipePortionsViewState.isValid() = portions.isEmpty() || portions.toShortOrNull() != null
