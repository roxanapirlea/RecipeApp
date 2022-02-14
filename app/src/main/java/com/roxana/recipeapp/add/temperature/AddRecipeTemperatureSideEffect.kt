package com.roxana.recipeapp.add.temperature

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeTemperatureSideEffect

object Forward : AddRecipeTemperatureSideEffect()
object Back : AddRecipeTemperatureSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeTemperatureSideEffect()
