package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.CreationRecipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseFlowUseCase<Any?, CreationRecipe>() {
    override fun execute(input: Any?): Flow<CreationRecipe> =
        creationRepository.getRecipe()
}
