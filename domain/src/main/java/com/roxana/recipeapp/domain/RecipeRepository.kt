package com.roxana.recipeapp.domain

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun addRecipe(recipe: CreationRecipe)
    suspend fun updateRecipe(recipe: CreationRecipe)
    fun getRecipesSummary(
        nameQuery: String,
        totalTime: Short?,
        cookingTime: Short?,
        preparationTime: Short?,
        category: CategoryType?
    ): Flow<List<RecipeSummary>>
    suspend fun getRecipeById(id: Int): Recipe
    fun getRecipeByIdAsFlow(id: Int): Flow<Recipe>
    suspend fun addComment(recipeId: Int, comment: String)
    fun getMaxTotalTime(): Flow<Short?>
    fun getMaxCookingTime(): Flow<Short?>
    fun getMaxPreparationTime(): Flow<Short?>
    suspend fun deleteById(id: Int)
}
