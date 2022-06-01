package com.roxana.recipeapp.domain.home.filters

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMaxTimesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseFlowUseCase<Unit?, GetMaxTimesUseCase.Output>() {
    override fun execute(input: Unit?): Flow<Output> {
        return combine(
            recipeRepository.getMaxTotalTime(),
            recipeRepository.getMaxCookingTime(),
            recipeRepository.getMaxPreparationTime()
        ) { total, cooking, preparation ->
            Output(maxTotal = total, maxPreparation = preparation, maxCooking = cooking)
        }.flowOn(Dispatchers.IO)
    }

    data class Output(val maxTotal: Int?, val maxPreparation: Int?, val maxCooking: Int?)
}
