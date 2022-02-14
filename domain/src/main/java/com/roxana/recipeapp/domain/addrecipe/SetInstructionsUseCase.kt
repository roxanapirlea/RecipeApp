package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CreationInstruction
import javax.inject.Inject

class SetInstructionsUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<List<CreationInstruction>, Unit>() {
    override suspend fun execute(input: List<CreationInstruction>) {
        creationRepository.setInstructions(input)
    }
}
