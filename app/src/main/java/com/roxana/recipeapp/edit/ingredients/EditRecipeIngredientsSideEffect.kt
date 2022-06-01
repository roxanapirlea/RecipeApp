package com.roxana.recipeapp.edit.ingredients

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeIngredientsSideEffect

object Forward : EditRecipeIngredientsSideEffect()
object Back : EditRecipeIngredientsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeIngredientsSideEffect()
