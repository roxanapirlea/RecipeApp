package com.roxana.recipeapp.edit.categories

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeCategoriesSideEffect

object ForwardForCreation : EditRecipeCategoriesSideEffect()
object ForwardForEditing : EditRecipeCategoriesSideEffect()
object Back : EditRecipeCategoriesSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeCategoriesSideEffect()
