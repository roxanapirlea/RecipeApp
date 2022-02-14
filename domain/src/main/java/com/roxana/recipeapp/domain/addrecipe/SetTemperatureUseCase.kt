package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.Temperature
import javax.inject.Inject

class SetTemperatureUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<SetTemperatureUseCase.Input, Unit>() {
    override suspend fun execute(input: Input) {
        creationRepository.setTemperature(input.temperature, input.temperatureUnit)
    }

    data class Input(
        val temperature: Short?,
        val temperatureUnit: Temperature
    )
}
