package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType

data class AddRecipeViewState(
    val title: Title = Title(),
    val categories: List<Category> = emptyList(),
    val quantities: List<QuantityType> = emptyList(),
    val portions: PortionsState = PortionsState(),
    val ingredients: List<IngredientState> = emptyList()
) {
    val isValid =
        title.isValid && portions.isValid && ingredients.all(IngredientState::isQuantityValid)
}

data class Title(val name: String = "", val isValid: Boolean = false)

data class Category(val type: CategoryType, val isSelected: Boolean)

data class PortionsState(
    val value: Short? = null,
    val text: String = "",
    val isValid: Boolean = true
)

data class IngredientState(
    val name: String = "",
    val quantityValue: Double? = null,
    val quantityText: String = "",
    val quantityType: QuantityType? = null,
    val isQuantityValid: Boolean = true,
    val isEditing: Boolean = false
) {
    val isEmpty = name.isBlank() && quantityText.isBlank()
}
