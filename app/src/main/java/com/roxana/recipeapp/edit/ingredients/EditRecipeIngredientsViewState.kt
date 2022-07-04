package com.roxana.recipeapp.edit.ingredients

import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiQuantityType

data class EditRecipeIngredientsViewState(
    val ingredients: List<IngredientState> = emptyList(),
    val editingIngredient: IngredientState = IngredientState(),
    val quantityTypes: List<UiQuantityType> = emptyList(),
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
    val navigation: Navigation? = null,
)

data class IngredientState(
    val id: Int? = null,
    val name: String = "",
    val quantity: String = "",
    val quantityType: UiQuantityType = UiQuantityType.None
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Close : Navigation()
    data class ToPage(val page: PageType) : Navigation()
}

fun IngredientState.isQuantityValid() = quantity.isEmpty() || quantity.toDoubleOrNull() != null
fun IngredientState.isEmpty() = name.isEmpty()
