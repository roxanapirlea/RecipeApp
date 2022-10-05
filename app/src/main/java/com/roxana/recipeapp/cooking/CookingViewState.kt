package com.roxana.recipeapp.cooking

import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

data class CookingViewState(
    val id: Int = 0,
    val title: String = "",
    val portions: Short? = null,
    val selectedPortions: Double = 1.0,
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<InstructionState> = emptyList(),
    val commentState: CommentState = CommentState(),
    val time: TimeState = TimeState(),
    val temperature: Short? = null,
    val temperatureUnit: UiTemperature? = null,
    val isLoading: Boolean = true,
    val isFetchingError: Boolean = false
)

data class IngredientState(
    val id: Int,
    val name: String,
    val quantity: Double?,
    val quantityForSelectedPortion: Double?,
    val quantityType: UiQuantityType,
    val isChecked: Boolean = false
)

data class InstructionState(
    val id: Short,
    val instruction: String,
    val isChecked: Boolean = false,
    val isCurrent: Boolean = false
)

data class TimeState(
    val total: Short? = null,
    val cooking: Short? = null,
    val waiting: Short? = null,
    val preparation: Short? = null,
) {
    val isEmpty = total == null && cooking == null && preparation == null && waiting == null
}

data class CommentState(
    val comments: List<Comment> = emptyList(),
    val isEditing: Boolean = false
)

data class Comment(
    val text: String,
    val id: Int,
)
