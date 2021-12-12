package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType

sealed class AddRecipeViewAction

data class TitleChanged(val name: String) : AddRecipeViewAction()
object Back : AddRecipeViewAction()
data class CategoryClicked(val type: CategoryType) : AddRecipeViewAction()
