package com.roxana.recipeapp.edit.temperature

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeTemperatureSideEffect

object ForwardForCreation : EditRecipeTemperatureSideEffect()
object ForwardForEditing : EditRecipeTemperatureSideEffect()
object Close : EditRecipeTemperatureSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeTemperatureSideEffect()
