package com.roxana.recipeapp.domain.temperature

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavePreferredTemperatureUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<Temperature, Unit>() {
    override suspend fun execute(input: Temperature) {
        withContext(dispatchers.io) {
            settingsRepository.setPreferredTemperatureUnit(input)
        }
    }
}
