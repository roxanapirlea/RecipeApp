package com.roxana.recipeapp.data.di

import com.roxana.recipeapp.domain.Recipe
import com.roxana.recipeapp.domain.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor() : RecipeRepository {
    override suspend fun addRecipe(recipe: Recipe) {
        TODO("not implemented")
    }
}
