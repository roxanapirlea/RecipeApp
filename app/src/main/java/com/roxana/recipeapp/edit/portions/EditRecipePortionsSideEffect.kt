package com.roxana.recipeapp.edit.portions

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipePortionsSideEffect

object ForwardForCreation : EditRecipePortionsSideEffect()
object ForwardForEditing : EditRecipePortionsSideEffect()
object Close : EditRecipePortionsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipePortionsSideEffect()
