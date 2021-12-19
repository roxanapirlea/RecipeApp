package com.roxana.recipeapp.home

sealed class HomeViewAction

object AddRecipe : HomeViewAction()
data class RecipeDetail(val id: Int) : HomeViewAction()
