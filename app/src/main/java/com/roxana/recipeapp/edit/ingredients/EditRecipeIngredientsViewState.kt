package com.roxana.recipeapp.edit.ingredients

import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiQuantityType

data class EditRecipeIngredientsViewState(
    val ingredients: List<IngredientState> = emptyList(),
    val editingIngredient: IngredientState = IngredientState(),
    val canAddIngredient: Boolean = false,
    val quantityTypes: List<UiQuantityType> = emptyList(),
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

data class IngredientState(
    val id: Int? = null,
    val name: String = "",
    val quantity: String = "",
    val isQuantityError: Boolean = false,
    val quantityType: UiQuantityType = UiQuantityType.None
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}
