package com.roxana.recipeapp.domain

import com.roxana.recipeapp.domain.model.CreationRecipe

interface RecipeRepository {
    suspend fun addRecipe(recipe: CreationRecipe)
}
