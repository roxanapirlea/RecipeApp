package com.roxana.recipeapp.add.categories

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeCategoriesSideEffect

object Forward : AddRecipeCategoriesSideEffect()
object Back : AddRecipeCategoriesSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeCategoriesSideEffect()
