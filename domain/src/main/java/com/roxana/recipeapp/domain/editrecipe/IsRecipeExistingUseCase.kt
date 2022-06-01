package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class IsRecipeExistingUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<Unit?, Boolean>() {
    override suspend fun execute(input: Unit?): Boolean =
        creationRepository.isRecipeExisting()
}
