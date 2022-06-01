package com.roxana.recipeapp.edit.categories

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeCategoriesSideEffect

object Forward : EditRecipeCategoriesSideEffect()
object Back : EditRecipeCategoriesSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeCategoriesSideEffect()
