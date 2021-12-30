package com.roxana.recipeapp.cooking

sealed class CookingViewAction

object Back : CookingViewAction()
object IncrementPortions : CookingViewAction()
object DecrementPortions : CookingViewAction()
object ResetPortions : CookingViewAction()
object ModifyQuantitiesByIngredient : CookingViewAction()
data class ToggleIngredientCheck(
    val ingredientId: Int,
    val isChecked: Boolean
) : CookingViewAction()

data class ToggleInstructionCheck(
    val instructionId: Short,
    val isChecked: Boolean
) : CookingViewAction()

object AddComment : CookingViewAction()
