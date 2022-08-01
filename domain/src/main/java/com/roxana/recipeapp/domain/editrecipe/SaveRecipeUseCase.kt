package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.PhotoRepository
import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val creationRepository: RecipeCreationRepository,
    private val photoRepository: PhotoRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<Any?, Unit>() {

    override suspend fun execute(input: Any?) {
        withContext(dispatchers.io) {
            val creationRecipe = creationRepository.getRecipe().first()
            val photoPath = creationRecipe.photoPath?.let {
                photoRepository.copyTempFileToPermFile(it)
            }
            val isExistingRecipe = creationRecipe.id != null

            if (isExistingRecipe)
                recipeRepository.updateRecipe(creationRecipe.copy(photoPath = photoPath))
            else
                recipeRepository.addRecipe(creationRecipe.copy(photoPath = photoPath))

            try {
                creationRepository.reset()
            } catch (e: Throwable) {
                // do nothing
            }
        }
    }
}
