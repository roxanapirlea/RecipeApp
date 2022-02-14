package com.roxana.recipeapp.add.ingredients

import com.roxana.recipeapp.uimodel.UiQuantityType

data class AddRecipeIngredientsViewState(
    val ingredients: List<IngredientState> = emptyList(),
    val editingIngredient: IngredientState = IngredientState(),
    val quantityTypes: List<UiQuantityType> = emptyList()
)

data class IngredientState(
    val name: String = "",
    val quantity: String = "",
    val quantityType: UiQuantityType = UiQuantityType.None
)

fun IngredientState.isQuantityValid() = quantity.isEmpty() || quantity.toDoubleOrNull() != null
fun IngredientState.isEmpty() = name.isEmpty()
