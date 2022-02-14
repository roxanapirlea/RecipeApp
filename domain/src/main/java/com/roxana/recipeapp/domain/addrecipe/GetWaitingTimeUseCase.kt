package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWaitingTimeUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseFlowUseCase<Any?, Short?>() {
    override fun execute(input: Any?): Flow<Short?> =
        creationRepository.getTimeWaiting()
}
