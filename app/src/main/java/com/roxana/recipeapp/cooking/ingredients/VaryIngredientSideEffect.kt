package com.roxana.recipeapp.cooking.ingredients

sealed class VaryIngredientSideEffect

data class ValidateSuccess(
    val portionsMultiplier: Double,
    val recipeId: Int
) : VaryIngredientSideEffect()
