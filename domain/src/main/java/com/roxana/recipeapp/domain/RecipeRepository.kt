package com.roxana.recipeapp.domain

import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun addRecipe(recipe: CreationRecipe)
    fun getRecipesSummary(): Flow<List<RecipeSummary>>
}
