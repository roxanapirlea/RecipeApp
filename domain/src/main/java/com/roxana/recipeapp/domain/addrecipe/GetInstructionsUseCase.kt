package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.CreationInstruction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInstructionsUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseFlowUseCase<Any?, List<CreationInstruction>>() {
    override fun execute(input: Any?): Flow<List<CreationInstruction>> =
        creationRepository.getInstructions()
}
