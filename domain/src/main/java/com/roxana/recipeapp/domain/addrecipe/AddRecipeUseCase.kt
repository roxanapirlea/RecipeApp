package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CreationRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseSuspendableUseCase<CreationRecipe, Unit>() {

    override suspend fun execute(input: CreationRecipe) {
        withContext(Dispatchers.IO) {
            recipeRepository.addRecipe(input)
        }
    }
}
