package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType

data class AddRecipeViewState(
    val title: Title = Title(),
    val categories: List<Category> = emptyList(),
    val quantities: List<QuantityType> = emptyList(),
    val portions: PortionsState = PortionsState(),
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<EditableState> = emptyList(),
    val comments: List<EditableState> = emptyList(),
    val time: TimeState = TimeState(),
    val temperature: TemperatureState = TemperatureState()
) {
    val isValid = title.isValid &&
        portions.isValid &&
        ingredients.all(IngredientState::isQuantityValid) &&
        time.isCookingValid &&
        time.isPreparationValid &&
        time.isWaitingValid &&
        time.isTotalValid &&
        temperature.isValid
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

data class EditableState(
    val name: String = "",
    val isEditing: Boolean = false
)

data class TimeState(
    val cooking: Short? = null,
    val cookingText: String = "",
    val isCookingValid: Boolean = true,
    val preparation: Short? = null,
    val preparationText: String = "",
    val isPreparationValid: Boolean = true,
    val waiting: Short? = null,
    val waitingText: String = "",
    val isWaitingValid: Boolean = true,
    val total: Short? = null,
    val totalText: String = "",
    val isTotalValid: Boolean = true,
)

data class TemperatureState(
    val value: Short? = null,
    val text: String = "",
    val isValid: Boolean = true
)
