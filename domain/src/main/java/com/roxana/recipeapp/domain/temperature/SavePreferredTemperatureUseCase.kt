package com.roxana.recipeapp.domain.temperature

import com.roxana.recipeapp.domain.SettingsRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.Temperature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavePreferredTemperatureUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseSuspendableUseCase<Temperature, Unit>() {
    override suspend fun execute(input: Temperature) {
        withContext(Dispatchers.IO) {
            settingsRepository.setPreferredTemperatureUnit(input)
        }
    }
}
