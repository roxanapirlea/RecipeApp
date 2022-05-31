package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CreationRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<Any?, Unit>() {

    override suspend fun execute(input: Any?) {
        withContext(Dispatchers.IO) {
            val creationRecipe = creationRepository.getRecipe().first()
            recipeRepository.addRecipe(creationRecipe)
            try {
                creationRepository.reset()
            } catch (e: Throwable) {
                // do nothing
            }
        }
    }
}
