package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class ResetRecipeUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<Any?, Unit>() {
    override suspend fun execute(input: Any?) {
        creationRepository.reset()
    }
}
