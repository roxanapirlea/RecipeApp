package com.roxana.recipeapp.domain.quantities

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.QuantityType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPreferredQuantitiesUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseFlowUseCase<Any?, List<QuantityType>>() {
    override fun execute(input: Any?): Flow<List<QuantityType>> =
        settingsRepository.getSelectedMeasuringUnits()
            .flowOn(Dispatchers.IO)
}
