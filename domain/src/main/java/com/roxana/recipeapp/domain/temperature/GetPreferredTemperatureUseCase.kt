package com.roxana.recipeapp.domain.temperature

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPreferredTemperatureUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseFlowUseCase<Any?, Temperature>() {
    override fun execute(input: Any?): Flow<Temperature> =
        settingsRepository.getPreferredTemperatureUnit()
}
