package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class SetTimeUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<SetTimeUseCase.Input, Unit>() {
    override suspend fun execute(input: Input) {
        creationRepository.setTime(input.cooking, input.preparation, input.waiting, input.total)
    }

    data class Input(
        val cooking: Short?,
        val preparation: Short?,
        val waiting: Short?,
        val total: Short?
    )
}
