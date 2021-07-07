package com.roxana.recipeapp.domain

interface RecipeRepository {
    suspend fun addRecipe(recipe: Recipe)
}
