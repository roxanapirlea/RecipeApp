package com.roxana.recipeapp.add

import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

data class AddRecipeViewState(
    val title: NonEmptyFieldState = NonEmptyFieldState(),
    val categories: List<CategoryState> = emptyList(),
    val quantities: List<UiQuantityType> = emptyList(),
    val portions: ShortFieldState = ShortFieldState(),
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<EditingState> = emptyList(),
    val comments: List<EditingState> = emptyList(),
    val time: TimeState = TimeState(),
    val temperature: ShortFieldState = ShortFieldState()
) {
    val isValid = title.isValid &&
        portions.isValid &&
        ingredients.all { it.isValid } &&
        time.cooking.isValid &&
        time.preparation.isValid &&
        time.waiting.isValid &&
        time.total.isValid &&
        temperature.isValid
}

data class CategoryState(val type: UiCategoryType, val isSelected: Boolean)

data class IngredientState(
    val name: EmptyFieldState = EmptyFieldState(),
    val quantity: DoubleFieldState = DoubleFieldState(),
    val quantityType: UiQuantityType = UiQuantityType.None,
    val isEditing: Boolean = false
) {
    val isEmpty = name.text.isBlank() && quantity.text.isBlank()
    val isValid = name.isValid && quantity.isValid
}

data class EditingState(
    val fieldState: EmptyFieldState = EmptyFieldState(),
    val isEditing: Boolean = true
)

data class TimeState(
    val cooking: ShortFieldState = ShortFieldState(),
    val preparation: ShortFieldState = ShortFieldState(),
    val waiting: ShortFieldState = ShortFieldState(),
    val total: ShortFieldState = ShortFieldState(),
)
