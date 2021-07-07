package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.Recipe
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseSuspendableUseCase<Recipe, Unit>() {

    override suspend fun execute(input: Recipe) {
        withContext(Dispatchers.IO) {
            recipeRepository.addRecipe(input)
        }
    }
}
