package com.roxana.recipeapp.cooking.ingredients

import com.roxana.recipeapp.uimodel.UiQuantityType

data class VaryIngredientsState(
    val ingredients: List<IngredientState> = emptyList(),
    val updatedIngredient: IngredientState? = null,
    val recipeId: Int? = null,
    val validation: Validation? = null
)

data class IngredientState(
    val id: Int,
    val name: String,
    val quantity: Double,
    val quantityText: String,
    val quantityType: UiQuantityType,
    val isQuantityInError: Boolean = false
)

data class Validation(
    val recipeId: Int,
    val portionsMultiplier: Double
)
