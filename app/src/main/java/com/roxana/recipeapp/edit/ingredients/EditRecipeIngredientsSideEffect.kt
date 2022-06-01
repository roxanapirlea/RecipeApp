package com.roxana.recipeapp.edit.ingredients

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeIngredientsSideEffect

object ForwardForCreation : EditRecipeIngredientsSideEffect()
object ForwardForEditing : EditRecipeIngredientsSideEffect()
object Back : EditRecipeIngredientsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeIngredientsSideEffect()
