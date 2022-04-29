package com.roxana.recipeapp.add.title

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeTitleSideEffect

object Forward : AddRecipeTitleSideEffect()
object Back : AddRecipeTitleSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeTitleSideEffect()
object RevealBackdrop : AddRecipeTitleSideEffect()